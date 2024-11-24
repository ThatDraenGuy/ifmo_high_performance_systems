package ru.draen.hps.billing.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.I18n;
import ru.draen.hps.billing.producer.dto.BillingPerformedMsg;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.messaging.model.AppMessage;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingPerformedProducer {
    private static final ILabelService lbs = I18n.getLabelService();

    private final KafkaTemplate<String, AppMessage> kafkaTemplate;

    @Value("${app.kafka.topics.billing-performed}")
    private String billingPerformedTopic;


    public Mono<Void> send(Long fileId) {
        return Mono.fromFuture(kafkaTemplate
                        .send(billingPerformedTopic, new BillingPerformedMsg(fileId))
                        .whenComplete((res, e) -> {
                            if (!isNull(e)) {
                                log.error(lbs.msg("errors.messaging.billingPerformed", e));
                            }
                        }))
                .then();
    }
}
