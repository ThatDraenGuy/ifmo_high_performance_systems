package ru.draen.hps.app.operator.dao;

import ru.draen.hps.common.dao.ICrudRepository;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.Operator;

public interface OperatorRepository extends ISearchRepository<Operator, Long>, ICrudRepository<Operator, Long> {
}
