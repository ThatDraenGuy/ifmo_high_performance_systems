package ru.draen.hps.cdr.app.cdrdata.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.draen.hps.cdr.app.cdrdata.dao.CdrDataRepository;
import ru.draen.hps.cdr.app.cdrdata.controller.dto.CdrDataDto;
import ru.draen.hps.cdr.client.AccountClient;
import ru.draen.hps.cdr.common.model.ClientModel;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;


@Service
@AllArgsConstructor
public class CdrDataServiceImpl implements CdrDataService {
    private final AccountClient accountClient;

    private final CdrDataRepository cdrDataRepository;

    @Override
    public Flux<ClientModel> findClients(Long fileId) {
        return cdrDataRepository.findClientIds(fileId).flatMap(accountClient::findClientById);
    }

    @Override
    @Transactional
    public Flux<CdrData> findByClient(Long fileId, Long clientId) {
        return cdrDataRepository.findByClient(fileId, clientId);
    }

    @Override
    @Transactional
    public Flux<CdrData> save(Flux<CdrData> calls) {
        return cdrDataRepository.saveAll(calls);
    }

    @Override
    public Mono<CdrData> update(Mono<CdrData> calls) {
        return calls
                .flatMap(call -> cdrDataRepository.findById(call.getId()).zipWith(Mono.just(call)))
                .map(this::applyUpdate)
                .flatMap(cdrDataRepository::save);
    }

    @Override
    public Mono<Void> deleteByFile(Long fileId) {
        return cdrDataRepository.deleteByFile(TimestampHelper.current(), fileId);
    }

    private CdrData applyUpdate(Tuple2<CdrData, CdrData> calls) {
        CdrData fromDb = calls.getT1();
        CdrData entity = calls.getT2();

        fromDb.setReportId(entity.getReportId());
        fromDb.setMinutes(entity.getMinutes());
        fromDb.setCost(entity.getCost());

        return fromDb;
    }
}
