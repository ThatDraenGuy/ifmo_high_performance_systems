package ru.draen.hps.file.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.service.FileService;

@Controller
@AllArgsConstructor
public class FileRSocketController {
    private final FileService fileService;
    private final IMapper<File, FileDto> fileMapper;

    @MessageMapping("/files/{id}")
    public Mono<FileDto> getFile(@DestinationVariable("id") Long id) {
        return fileService.getWithContent(id)
                .map(fileMapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException::new));
    }
}
