package ru.draen.hps.cdr.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.I18n;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.cdr.producer.dto.CdrFileParsedMsg;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.messaging.model.AppMessage;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdrFileParsedProducer {
    private static final ILabelService lbs = I18n.getLabelService();

    private final KafkaTemplate<String, AppMessage> kafkaTemplate;

    @Value("${app.kafka.topics.cdr-file-parsed}")
    private String fileParsedTopic;

    public Mono<CdrFileDto> send(CdrFileDto cdrFile) {
        return Mono.fromFuture(kafkaTemplate
                        .send(fileParsedTopic, new CdrFileParsedMsg(cdrFile.getFileId()))
                        .whenComplete((res, e) -> {
                            if (!isNull(e)) {
                                log.error(lbs.msg("errors.messaging.fileParsed", e));
                            }
                        }))
                .then(Mono.just(cdrFile));
    }
}
