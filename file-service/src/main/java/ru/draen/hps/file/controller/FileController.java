package ru.draen.hps.file.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.file.controller.dto.FileBriefDto;
import ru.draen.hps.file.controller.dto.FileCondition;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.service.FileService;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.core.validation.groups.Create;

@RestController
@RequestMapping(value = "${api.prefix}/files", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class FileController {
    private final FileService fileService;
    private final IMapper<File, FileBriefDto> fileBriefMapper;
    private final IMapper<File, FileDto> fileMapper;

    @GetMapping
    public Flux<FileBriefDto> find(@Validated FileCondition condition) {
        return fileService.findAll(condition).map(fileBriefMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<FileDto> get(@PathVariable("id") Long id) {
        return fileService.getWithContent(id).map(fileMapper::toDto);
    }

    @PostMapping("/upload")
    public Mono<FileBriefDto> upload(@RequestBody @Validated(Create.class) FileDto dto) {
        return fileService.create(fileMapper.toEntity(dto)).map(fileBriefMapper::toDto);
    }

//    @PostMapping("/upload-local")
//    public ResponseEntity<FileBriefDto> uploadLocal(@RequestBody @Validated UploadLocalFileRequest request) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(
//                fileBriefMapper.toDto(fileService.createFromLocal(request))
//        );
//    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable("id") Long id) {
        return fileService.delete(id)
                ? Mono.empty()
                : Mono.empty(); //TODO think
    }
}
