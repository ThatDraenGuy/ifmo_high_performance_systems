package ru.draen.hps.app.tariffrule.dao;

import ru.draen.hps.common.dao.ICrudRepository;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.TariffRule;

public interface TariffRuleRepository extends ISearchRepository<TariffRule, Long>, ICrudRepository<TariffRule, Long> {
}
