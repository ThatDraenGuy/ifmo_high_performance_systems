package ru.draen.hps.cdr.app.cdrfile.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.r2dbcdao.domain.CdrFile;

@Repository
public interface CdrFileRepository extends ReactiveCrudRepository<CdrFile, Long> {
}
