package ru.draen.hps.file.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.core.validation.groups.Create;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.file.controller.dto.FileBriefDto;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.producer.FileUploadedProducer;
import ru.draen.hps.file.service.FileService;

@Controller
@AllArgsConstructor
public class FileRSocketController {
    private final FileService fileService;
    private final FileUploadedProducer fileUploadedProducer;
    private final IMapper<File, FileDto> fileMapper;
    private final IMapper<File, FileBriefDto> fileBriefMapper;


    @MessageMapping("/files/{id}")
    public Mono<FileDto> getFile(@DestinationVariable("id") Long id) {
        return fileService.getWithContent(id)
                .map(fileMapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException::new));
    }

    @MessageMapping("/files/upload")
    public Mono<FileBriefDto> upload(@Payload @Validated(Create.class) FileDto dto) {
        return fileService.create(fileMapper.toEntity(dto))
                .map(fileBriefMapper::toDto)
                .flatMap(fileUploadedProducer::send);
    }
}
