package ru.draen.hps.app.cdrfile.dao;

import ru.draen.hps.common.dao.ICrudRepository;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.CdrFile;

public interface CdrFileRepository extends ISearchRepository<CdrFile, Long>, ICrudRepository<CdrFile, Long> {
}
