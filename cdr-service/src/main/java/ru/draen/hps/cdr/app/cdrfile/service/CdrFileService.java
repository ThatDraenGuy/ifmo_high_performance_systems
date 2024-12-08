package ru.draen.hps.cdr.app.cdrfile.service;


import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;

public interface CdrFileService {
    Mono<CdrFileDto> parseData(Long fileId);
    Mono<CdrFileDto> findById(Long fileId);
    Mono<Void> delete(Long fileId);
//    CdrFile parseData(Long fileId);
//    Optional<CdrFile> findById(Long fileId);
//    Stream<Client> getClients(CdrFile entity);
}
