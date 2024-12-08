package ru.draen.hps.cdr.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.ParseCdrRequest;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;
import ru.draen.hps.common.webflux.saga.SagaStep;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CdrFileCancelListener {
    private final SagaStep<ParseCdrRequest, CdrFileDto> cdrSagaStep;


    @KafkaListener(topics = "${app.kafka.topics.cdr-file-cancel}", properties = {"spring.json.value.default.type=ru.draen.hps.common.messaging.model.FileRelatedMsg"})
    public Mono<Void> listenCdrFileCancel(@Payload FileRelatedMsg msg) {
        return cdrSagaStep.cancel(new ParseCdrRequest(msg.fileId()));
    }
}
