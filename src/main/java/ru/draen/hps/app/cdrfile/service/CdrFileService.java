package ru.draen.hps.app.cdrfile.service;

import ru.draen.hps.domain.CdrFile;

public interface CdrFileService {
    CdrFile parseData(Long fileId);
}
