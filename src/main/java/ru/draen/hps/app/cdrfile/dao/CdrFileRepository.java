package ru.draen.hps.app.cdrfile.dao;

import ru.draen.hps.common.dao.ICrudRepository;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.CdrFile;
import ru.draen.hps.domain.Client;

import java.util.stream.Stream;

public interface CdrFileRepository extends ISearchRepository<CdrFile, Long>, ICrudRepository<CdrFile, Long> {
    Stream<Client> getClients(CdrFile entity);
}
