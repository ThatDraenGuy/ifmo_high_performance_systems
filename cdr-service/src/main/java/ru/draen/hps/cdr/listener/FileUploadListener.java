package ru.draen.hps.cdr.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.app.cdrfile.service.CdrFileService;
import ru.draen.hps.cdr.listener.dto.FileUploadedMsg;
import ru.draen.hps.cdr.producer.CdrFileParsedProducer;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileUploadListener {
    private final CdrFileService cdrFileService;
    private final CdrFileParsedProducer cdrFileParsedProducer;

    @KafkaListener(topics = "${app.kafka.topics.file-uploaded}", properties = {"spring.json.value.default.type=ru.draen.hps.cdr.listener.dto.FileUploadedMsg"})
    public Mono<Void> listenFileUploaded(@Payload FileUploadedMsg fileUploadedMsg) {
        return cdrFileService.parseData(fileUploadedMsg.fileId()).flatMap(cdrFileParsedProducer::send).then();
    }
}
