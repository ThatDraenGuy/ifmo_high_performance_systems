package ru.draen.hps.app.cdrfile.service;

import ru.draen.hps.domain.CdrFile;
import ru.draen.hps.domain.Client;

import java.util.Optional;
import java.util.stream.Stream;

public interface CdrFileService {
    CdrFile parseData(Long fileId);
    Optional<CdrFile> findById(Long fileId);
    Stream<Client> getClients(CdrFile entity);
}
