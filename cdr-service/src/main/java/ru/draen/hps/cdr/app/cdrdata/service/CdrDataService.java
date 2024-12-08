package ru.draen.hps.cdr.app.cdrdata.service;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.app.cdrdata.controller.dto.CdrDataDto;
import ru.draen.hps.cdr.common.model.ClientModel;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;

public interface CdrDataService {
    Flux<ClientModel> findClients(Long fileId);
    Flux<CdrData> findByClient(Long fileId, Long clientId);
    Flux<CdrData> save(Flux<CdrData> calls);
    Mono<CdrData> update(Mono<CdrData> calls);
    Mono<Void> deleteByFile(Long fileId);
}
