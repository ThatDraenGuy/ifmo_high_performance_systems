package ru.draen.hps.file.service;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.file.controller.dto.FileCondition;

public interface FileService {
    Flux<File> findAll(@NonNull FileCondition condition);
    Mono<File> getWithContent(@NonNull Long id);
    Mono<File> create(@NonNull File file);
    Mono<Boolean> delete(@NonNull Long id);
}
