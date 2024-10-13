package ru.draen.hps.app.cdrfile.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.I18n;
import ru.draen.hps.app.cdrdata.service.CdrDataService;
import ru.draen.hps.app.cdrdata.service.csv.CdrDataItem;
import ru.draen.hps.app.cdrfile.dao.CdrFileRepository;
import ru.draen.hps.app.cdrfile.service.filename.CdrFileName;
import ru.draen.hps.app.cdrfile.service.filename.CdrFileNameParser;
import ru.draen.hps.app.client.controller.dto.ClientCondition;
import ru.draen.hps.app.client.service.ClientService;
import ru.draen.hps.app.file.dao.FileContentFetchProfile;
import ru.draen.hps.app.file.dao.FileContentRepository;
import ru.draen.hps.common.csv.ICsvParser;
import ru.draen.hps.common.exception.NotFoundException;
import ru.draen.hps.common.exception.ProcessingException;
import ru.draen.hps.common.label.ILabelService;
import ru.draen.hps.common.utils.TimestampHelper;
import ru.draen.hps.domain.*;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CdrFileServiceImpl implements CdrFileService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TransactionTemplate transactionTemplate;
    private final ICsvParser<CdrDataItem> dataParser;
    private final CdrFileNameParser cdrFileNameParser;
    private final CdrFileRepository cdrFileRepository;
    private final CdrDataService cdrDataService;
    private final FileContentRepository fileContentRepository;
    private final ClientService clientService;

    @Override
    public CdrFile parseData(Long fileId) {
        return transactionTemplate.execute(status -> {
            FileContent fileContent = fileContentRepository.findById(fileId, FileContentFetchProfile::withFile).orElseThrow(NotFoundException::new);

            CdrFileName fileName = cdrFileNameParser.parse(fileContent.getFile().getFileName());
            Operator operator = fileContent.getFile().getOperator();
            if (!operator.getCode().equals(fileName.operCode())) {
                throw new ProcessingException(lbs.msg("ProcessingException.CdrFileService.wrongOperator"));
            }
            CdrFile cdrFile = new CdrFile();
            cdrFile.setFile(fileContent.getFile());
            cdrFile.setStartTime(TimestampHelper.toInstant(fileName.periodStart()));
            cdrFile.setEndTime(TimestampHelper.toInstant(fileName.periodEnd()));

            CdrFile entity = cdrFileRepository.save(cdrFile);

            dataParser.parse(fileContent, item -> {
                CdrData data = new CdrData();
                data.setCdrFile(entity);
                data.setDirection(item.getCallDirection());
                data.setStartTime(TimestampHelper.toInstant(item.getStartTime()));
                data.setEndTime(TimestampHelper.toInstant(item.getEndTime()));
                if (data.getStartTime().isBefore(cdrFile.getStartTime())
                        || data.getEndTime().isAfter(cdrFile.getEndTime())
                        || data.getStartTime().isAfter(data.getEndTime())) {
                    throw new ProcessingException(lbs.msg("ProcessingException.CdrFileService.invalidCallTime"));
                }

                Client client = clientService
                        .findOneBrief(new ClientCondition(item.getPhoneNumber(), operator.getId()))
                        .orElseThrow(NotFoundException::new);
                data.setClient(client);
                cdrDataService.save(data);
            });
            return cdrFile;
        });
    }

    @Override
    public Optional<CdrFile> findById(Long fileId) {
        return readOnlyTransactionTemplate.execute(status -> cdrFileRepository.findById(fileId));
    }

    @Override
    public Stream<Client> getClients(CdrFile entity) {
        return readOnlyTransactionTemplate.execute(status -> cdrFileRepository.getClients(entity));
    }
}
