package ru.draen.hps.app.file.service;

import ru.draen.hps.app.file.controller.dto.FileCondition;
import ru.draen.hps.app.file.controller.dto.UploadLocalFileRequest;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.File;

import java.util.Optional;

public interface FileService extends ISearchService<File, FileCondition> {
    Optional<File> getWithContent(Long fileId);
    File createFromLocal(UploadLocalFileRequest request);
    File create(File file);
    boolean delete(Long fileId);
}
