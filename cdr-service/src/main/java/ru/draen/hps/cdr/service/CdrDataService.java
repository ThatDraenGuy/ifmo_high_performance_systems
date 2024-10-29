package ru.draen.hps.cdr.service;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;

public interface CdrDataService {
    Flux<CdrData> findByClient(Long fileId, Long clientId);
    Flux<CdrData> save(Flux<CdrData> calls);
}
