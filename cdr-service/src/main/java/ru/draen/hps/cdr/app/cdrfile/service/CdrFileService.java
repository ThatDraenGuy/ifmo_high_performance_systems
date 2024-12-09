package ru.draen.hps.cdr.app.cdrfile.service;


import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;

public interface CdrFileService {
    Mono<CdrFileDto> parseData(Long fileId);
    Mono<CdrFileDto> findById(Long fileId);
    Mono<Void> delete(Long fileId);
}
