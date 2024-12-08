package ru.draen.hps.cdr.app.cdrfile.service;

import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.I18n;
import ru.draen.hps.cdr.app.cdrfile.dao.CdrFileCreateRepository;
import ru.draen.hps.cdr.client.AccountClient;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.cdr.app.cdrdata.service.CdrDataService;
import ru.draen.hps.cdr.app.cdrdata.service.csv.CdrDataItem;
import ru.draen.hps.cdr.app.cdrfile.dao.CdrFileRepository;
import ru.draen.hps.cdr.app.cdrfile.service.filename.CdrFileName;
import ru.draen.hps.cdr.app.cdrfile.service.filename.CdrFileNameParser;
import ru.draen.hps.cdr.client.FileRSocketClient;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.exception.ProcessingException;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.csv.ICsvParser;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.r2dbcdao.domain.*;
import ru.draen.hps.common.webflux.utils.CacheUtils;

import java.io.ByteArrayInputStream;

@Service
@AllArgsConstructor
public class CdrFileServiceImpl implements CdrFileService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final ICsvParser<CdrDataItem> dataParser;
    private final CdrFileNameParser cdrFileNameParser;
    private final CdrFileRepository cdrFileRepository;
    private final CdrFileCreateRepository cdrFileCreateRepository;
    private final CdrDataService cdrDataService;

    private final AccountClient accountClient;
    private final FileRSocketClient fileRSocketClient;
    private final IMap<Long, CdrFileDto> cdrFileCache;

    @Override
    @Transactional
    public Mono<CdrFileDto> parseData(Long fileId) {
        return fileRSocketClient.getFile(fileId).flatMap(file -> {

            CdrFileName fileName = cdrFileNameParser.parse(file.getFileName());
            return accountClient.findOperatorById(file.getOperator().getOperatorId()).flatMap(operator -> {
                if (!operator.getCode().equals(fileName.operCode())) {
                    throw new ProcessingException(
                            lbs.msg("ProcessingException.CdrFileService.wrongOperator"));
                }
                CdrFileCreate cdrFile = new CdrFileCreate();
                cdrFile.setId(file.getFileId());
                cdrFile.setStartTime(TimestampHelper.toInstant(fileName.periodStart()));
                cdrFile.setEndTime(TimestampHelper.toInstant(fileName.periodEnd()));

                return cdrFileCreateRepository.save(cdrFile).flatMap(newEntity -> {
                    Flux<CdrData> calls = Flux.fromStream(dataParser.parseStream(
                            new ByteArrayInputStream(file.getContent().getData())
                    )).flatMap(item -> {
                        CdrData data = new CdrData();
                        data.setCdrFileId(newEntity.getId());
                        data.setDirection(item.getCallDirection());
                        data.setStartTime(TimestampHelper.toInstant(item.getStartTime()));
                        data.setEndTime(TimestampHelper.toInstant(item.getEndTime()));
                        if (data.getStartTime().isBefore(cdrFile.getStartTime())
                                || data.getEndTime().isAfter(cdrFile.getEndTime())
                                || data.getStartTime().isAfter(data.getEndTime())) {
                            throw new ProcessingException(
                                    lbs.msg("ProcessingException.CdrFileService.invalidCallTime"));
                        }

                        return accountClient.findClient(item.getPhoneNumber())
                                .map(client -> {
                                    data.setClientId(client.getClientId());
                                    return data;
                                })
                                .switchIfEmpty(Mono.error(NotFoundException::new));
                    });
                    return cdrDataService.save(calls).then(Mono.just(CdrFileDto.of(newEntity.toEntity(), file)));
                });
            }).switchIfEmpty(Mono.error(NotFoundException::new));
        });
    }

    @Override
    @Transactional
    public Mono<CdrFileDto> findById(Long fileId) {
        return CacheUtils.cacheGet(cdrFileCache, fileId, () ->
                fileRSocketClient.getFile(fileId).flatMap(file ->
                        cdrFileRepository.findById(fileId).map(cdrFile -> CdrFileDto.of(cdrFile, file))));
    }

    @Override
    public Mono<Void> delete(Long fileId) {
        return cdrDataService.deleteByFile(fileId).then(cdrFileRepository.deleteById(fileId));
    }
}
