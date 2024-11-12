package ru.draen.hps.cdr.app.cdrfile.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.r2dbcdao.domain.CdrFile;
import ru.draen.hps.common.r2dbcdao.domain.CdrFileCreate;

@Repository
public interface CdrFileCreateRepository extends ReactiveCrudRepository<CdrFileCreate, Long> {
}
