package ru.draen.hps.cdr.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.r2dbcdao.domain.Operator;

@Repository
public interface OperatorRepository extends ReactiveCrudRepository<Operator, Long> {
}
