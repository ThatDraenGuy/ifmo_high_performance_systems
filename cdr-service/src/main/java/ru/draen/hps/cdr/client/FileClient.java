package ru.draen.hps.cdr.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.dto.FileDto;


@ReactiveFeignClient(value = "file-service", path = "${api.prefix}/files")
public interface FileClient {

    @GetMapping("/{id}")
    Mono<FileDto> getFile(@PathVariable("id") Long id);
}
