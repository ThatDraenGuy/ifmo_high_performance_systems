package ru.draen.hps.cdr.client;

import org.springframework.cloud.openfeign.FeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.FileContent;

@FeignClient
public interface FileClient {
    Mono<FileContent> getContent(Long id);
}
