package ru.draen.hps.app.file.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.draen.hps.app.file.controller.dto.FileBriefDto;
import ru.draen.hps.app.file.controller.dto.FileCondition;
import ru.draen.hps.app.file.controller.dto.FileDto;
import ru.draen.hps.app.file.controller.dto.UploadLocalFileRequest;
import ru.draen.hps.app.file.service.FileService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.domain.File;

@RestController
@RequestMapping(value = "${api.prefix}/files", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class FileController {
    private final FileService fileService;
    private final IMapper<File, FileBriefDto> fileBriefMapper;
    private final IMapper<File, FileDto> fileMapper;

    @GetMapping("/paged")
    public PageResponse<FileBriefDto> findPage(@Validated FileCondition condition, PageCondition pageCondition) {
        return fileService.findAll(condition, pageCondition).map(fileBriefMapper::toDto);
    }

    @GetMapping
    public ScrollResponse<FileBriefDto> find(@Validated FileCondition condition, ScrollCondition scrollCondition) {
        return fileService.findAll(condition, scrollCondition).map(fileBriefMapper::toDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> get(@PathVariable("id") Long id) {
        return ResponseEntity.of(
                fileService.getWithContent(id).map(fileMapper::toDto)
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<FileBriefDto> upload(@RequestBody @Validated(Create.class) FileDto dto) {
        return ResponseEntity.ok(
                fileBriefMapper.toDto(fileService.create(fileMapper.toEntity(dto)))
        );
    }

    @PostMapping("/upload-local")
    public ResponseEntity<FileBriefDto> uploadLocal(@RequestBody @Validated UploadLocalFileRequest request) {
        return ResponseEntity.ok(
                fileBriefMapper.toDto(fileService.createFromLocal(request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        return fileService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
