package ru.draen.hps.app.tariff.dao;

import ru.draen.hps.common.dao.IStreamRepository;
import ru.draen.hps.domain.TariffToRules;

import java.util.List;

public interface TariffToRulesRepository extends IStreamRepository<TariffToRules, TariffToRules.PK> {
    void save(List<TariffToRules> rules);
}
