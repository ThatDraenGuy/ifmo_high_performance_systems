package ru.draen.hps.cdr.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.I18n;
import ru.draen.hps.cdr.client.DomainClient;
import ru.draen.hps.cdr.client.FileClient;
import ru.draen.hps.cdr.service.csv.CdrDataItem;
import ru.draen.hps.cdr.dao.CdrFileRepository;
import ru.draen.hps.cdr.service.filename.CdrFileName;
import ru.draen.hps.cdr.service.filename.CdrFileNameParser;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.exception.ProcessingException;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.csv.ICsvParser;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.r2dbcdao.domain.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CdrFileServiceImpl implements CdrFileService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final ICsvParser<CdrDataItem> dataParser;
    private final CdrFileNameParser cdrFileNameParser;
    private final CdrFileRepository cdrFileRepository;
    private final DomainClient domainClient;
    private final CdrDataService cdrDataService;
    private final FileClient fileClient;

    @Override
    public Mono<CdrFile> parseData(Long fileId) {
        return fileClient.getContent(fileId).flatMap(fileContent -> {
            CdrFileName fileName = cdrFileNameParser.parse(fileContent.getFileName());
            return domainClient.findOperatorById(fileContent.getOperatorId()).flatMap(operator -> {
                if (!operator.getCode().equals(fileName.operCode())) {
                    throw new ProcessingException(lbs.msg("ProcessingException.CdrFileService.wrongOperator"));
                }
                CdrFile cdrFile = new CdrFile();
                cdrFile.setId(fileContent.getId());
                cdrFile.setStartTime(TimestampHelper.toInstant(fileName.periodStart()));
                cdrFile.setEndTime(TimestampHelper.toInstant(fileName.periodEnd()));

                return cdrFileRepository.save(cdrFile).flatMap(entity -> {
                    List<CdrData> calls = new ArrayList<>();
                    dataParser.parse(new ByteArrayInputStream(fileContent.getData()), item -> {
                        CdrData data = new CdrData();
                        data.setCdrFileId(entity.getId());
                        data.setDirection(item.getCallDirection());
                        data.setStartTime(TimestampHelper.toInstant(item.getStartTime()));
                        data.setEndTime(TimestampHelper.toInstant(item.getEndTime()));
                        if (data.getStartTime().isBefore(cdrFile.getStartTime())
                                || data.getEndTime().isAfter(cdrFile.getEndTime())
                                || data.getStartTime().isAfter(data.getEndTime())) {
                            throw new ProcessingException(lbs.msg("ProcessingException.CdrFileService.invalidCallTime"));
                        }

                        Client client = domainClient.findClient(item.getPhoneNumber()).blockOptional()
                                .orElseThrow(NotFoundException::new);
                        data.setClientId(client.getId());
                        calls.add(data);
                    });
                    return cdrDataService.save(Flux.fromIterable(calls)).then(Mono.just(entity));
                });
            });
        });
    }

    @Override
    @Transactional
    public Mono<CdrFile> findById(Long fileId) {
        return cdrFileRepository.findById(fileId);
    }
}
