package ru.draen.hps.file.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.file.controller.dto.FileBriefDto;
import ru.draen.hps.file.controller.dto.FileCondition;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.producer.FileUploadedProducer;
import ru.draen.hps.file.service.FileService;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.core.validation.groups.Create;

@RestController
@RequestMapping(value = "${api.prefix}/files", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class FileController {
    private final FileService fileService;
    private final FileUploadedProducer fileUploadedProducer;
    private final IMapper<File, FileBriefDto> fileBriefMapper;
    private final IMapper<File, FileDto> fileMapper;

    @GetMapping
    public Flux<FileBriefDto> find(@Validated FileCondition condition) {
        return fileService.findAll(condition).map(fileBriefMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<FileDto> get(@PathVariable("id") Long id) {
        return fileService.getWithContent(id).map(fileMapper::toDto).switchIfEmpty(Mono.error(NotFoundException::new));
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FileBriefDto> upload(@RequestBody @Validated(Create.class) FileDto dto) {
        return fileService.create(fileMapper.toEntity(dto)).map(fileBriefMapper::toDto).flatMap(fileUploadedProducer::send);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") Long id) {
        return fileService.delete(id).flatMap(result -> result ? Mono.empty() : Mono.error(NotFoundException::new));
    }
}
