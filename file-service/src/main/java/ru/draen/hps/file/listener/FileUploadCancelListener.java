package ru.draen.hps.file.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;
import ru.draen.hps.common.webflux.saga.SagaStep;
import ru.draen.hps.file.controller.dto.FileBriefDto;
import ru.draen.hps.file.controller.dto.FileDto;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileUploadCancelListener {
    private final SagaStep<FileDto, FileBriefDto> fileSagaStep;


    @KafkaListener(topics = "${app.kafka.topics.file-upload-cancel}", properties = {"spring.json.value.default.type=ru.draen.hps.common.messaging.model.FileRelatedMsg"})
    public Mono<Void> listenCdrFileCancel(@Payload FileRelatedMsg msg) {
        FileDto req = new FileDto();
        req.setFileId(msg.fileId());
        return fileSagaStep.cancel(req);
    }
}
