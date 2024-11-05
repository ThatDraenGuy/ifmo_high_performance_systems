package ru.draen.hps.account.app.operator.dao;

import ru.draen.hps.common.dbms.domain.Operator;
import ru.draen.hps.common.jpadao.dao.ICrudRepository;
import ru.draen.hps.common.jpadao.dao.ISearchRepository;

public interface OperatorRepository extends ISearchRepository<Operator, Long>, ICrudRepository<Operator, Long> {
}
