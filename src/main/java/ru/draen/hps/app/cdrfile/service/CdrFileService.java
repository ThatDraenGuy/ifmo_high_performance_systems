package ru.draen.hps.app.cdrfile.service;

import ru.draen.hps.domain.CdrFile;

import java.util.Optional;

public interface CdrFileService {
    CdrFile parseData(Long fileId);
    Optional<CdrFile> findById(Long fileId);
}
