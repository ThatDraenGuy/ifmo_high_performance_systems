package ru.draen.hps.billing.listener;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.billing.service.BillingService;
import ru.draen.hps.billing.listener.dto.CdrFileParsedMsg;
import ru.draen.hps.billing.producer.BillingPerformedProducer;

@Controller
@AllArgsConstructor
public class CdrFileParsedListener {
    private final BillingService billingService;
    private final BillingPerformedProducer billingPerformedProducer;

    @KafkaListener(topics = "${app.kafka.topics.cdr-file-parsed}", properties = {"spring.json.value.default.type=ru.draen.hps.billing.listener.dto.CdrFileParsedMsg"})
    public Mono<Void> listenCdrFileParsed(@Payload CdrFileParsedMsg cdrFileParsedMsg) {
        return billingService.perform(new BillingRequest(cdrFileParsedMsg.fileId()))
                .flatMap(billingPerformedProducer::send);
    }
}
