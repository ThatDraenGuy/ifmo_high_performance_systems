package ru.draen.hps.billing.listener;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;
import ru.draen.hps.common.webflux.saga.SagaStep;

@Controller
@AllArgsConstructor
public class CdrFileParsedListener {
    private final SagaStep<BillingRequest, Void> billingSagaStep;

    @KafkaListener(topics = "${app.kafka.topics.cdr-file-parsed}", properties = {"spring.json.value.default.type=ru.draen.hps.common.messaging.model.FileRelatedMsg"})
    public Mono<Void> listenCdrFileParsed(@Payload FileRelatedMsg msg) {
        return billingSagaStep.process(new BillingRequest(msg.fileId()));
    }
}
