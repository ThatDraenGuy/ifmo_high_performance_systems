package ru.draen.hps.cdr.client;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.common.model.FileModel;

public interface FileRSocketClient {
    @RSocketExchange("/files/{id}")
    Mono<FileModel> getFile(@DestinationVariable("id") Long fileId);
}
