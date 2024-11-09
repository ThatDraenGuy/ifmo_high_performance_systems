package ru.draen.hps.billing.app.tariff.dao;


import ru.draen.hps.common.dbms.domain.TariffToRules;
import ru.draen.hps.common.jpadao.dao.IStreamRepository;

import java.util.List;

public interface TariffToRulesRepository extends IStreamRepository<TariffToRules, TariffToRules.PK> {
    void save(List<TariffToRules> rules);
}
