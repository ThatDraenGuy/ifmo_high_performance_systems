package ru.draen.hps.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.draen.hps.notification.model.*;
import ru.draen.hps.notification.service.NotificationSink;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageListener {

    @KafkaListener(topics = "${app.kafka.topics.file-uploaded}", properties = {"spring.json.value.default.type=ru.draen.hps.notification.model.FileUploadedMsg"})
    public void listenFileUploaded(@Payload FileUploadedMsg msg) {
        NotificationSink.SINK.emitNext(new NotificationInfo("file-uploaded", msg.fileId()), Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @KafkaListener(topics = "${app.kafka.topics.cdr-file-parsed}", properties = {"spring.json.value.default.type=ru.draen.hps.notification.model.CdrFileParsedMsg"})
    public void listenCdrFileParsed(@Payload CdrFileParsedMsg msg) {
        NotificationSink.SINK.emitNext(new NotificationInfo("cdr-file-parsed", msg.fileId()), Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @KafkaListener(topics = "${app.kafka.topics.billing-performed}", properties = {"spring.json.value.default.type=ru.draen.hps.notification.model.BillingPerformedMsg"})
    public void listenBillingPerformed(@Payload BillingPerformedMsg msg) {
        NotificationSink.SINK.emitNext(new NotificationInfo("billing-performed", msg.fileId()), Sinks.EmitFailureHandler.FAIL_FAST);

    }
}
