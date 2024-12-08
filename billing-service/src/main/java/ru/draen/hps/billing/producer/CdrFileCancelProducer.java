package ru.draen.hps.billing.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.I18n;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.messaging.model.AppMessage;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdrFileCancelProducer {
    private static final ILabelService lbs = I18n.getLabelService();

    private final KafkaTemplate<String, AppMessage> kafkaTemplate;

    @Value("${app.kafka.topics.cdr-file-cancel}")
    private String cdrFileCancel;

    public<T> Mono<T> send(Long fileId, Throwable cause) {
        return Mono.fromFuture(kafkaTemplate
                        .send(cdrFileCancel, new FileRelatedMsg(fileId))
                        .whenComplete((res, e) -> {
                            if (!isNull(e)) {
                                log.error(lbs.msg("errors.messaging.billingPerformed", e));
                            }
                        }))
                .then(Mono.empty());
    }
}
