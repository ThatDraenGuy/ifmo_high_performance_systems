package ru.draen.hps.cdr.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.I18n;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.messaging.model.AppMessage;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadCancelProducer {
    private static final ILabelService lbs = I18n.getLabelService();

    private final KafkaTemplate<String, AppMessage> kafkaTemplate;

    @Value("${app.kafka.topics.file-upload-cancel}")
    private String fileUploadCancel;

    public<T> Mono<T> send(Long fileId, Throwable ignored) {
        return Mono.fromFuture(kafkaTemplate
                        .send(fileUploadCancel, new FileRelatedMsg(fileId))
                        .whenComplete((res, e) -> {
                            if (!isNull(e)) {
                                log.error(lbs.msg("errors.messaging.fileParsed", e));
                            }
                        }))
                .then(Mono.empty());
    }
}
