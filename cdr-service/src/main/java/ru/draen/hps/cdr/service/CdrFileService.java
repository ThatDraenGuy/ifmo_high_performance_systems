package ru.draen.hps.cdr.service;


import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.CdrFile;

public interface CdrFileService {
    Mono<CdrFile> parseData(Long fileId);
    Mono<CdrFile> findById(Long fileId);
//    CdrFile parseData(Long fileId);
//    Optional<CdrFile> findById(Long fileId);
//    Stream<Client> getClients(CdrFile entity);
}
