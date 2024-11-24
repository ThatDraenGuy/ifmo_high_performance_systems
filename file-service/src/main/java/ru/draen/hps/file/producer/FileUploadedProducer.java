package ru.draen.hps.file.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.messaging.model.AppMessage;
import ru.draen.hps.file.I18n;
import ru.draen.hps.file.controller.dto.FileBriefDto;
import ru.draen.hps.file.producer.dto.FileUploadedMsg;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUploadedProducer {
    private static final ILabelService lbs = I18n.getLabelService();

    private final KafkaTemplate<String, AppMessage> kafkaTemplate;

    @Value("${app.kafka.topics.file-uploaded}")
    private String fileUploadedTopic;

    public Mono<FileBriefDto> send(FileBriefDto file) {
        return Mono.fromFuture(kafkaTemplate
                        .send(fileUploadedTopic, new FileUploadedMsg(file.getFileId()))
                        .whenComplete((res, e) -> {
                            if (!isNull(e)) {
                                log.error(lbs.msg("errors.messaging.fileParsed", e));
                            }
                        }))
                .then(Mono.just(file));
    }
}
