package ru.draen.hps.app.tariffrule.service;

import lombok.NonNull;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleCondition;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.TariffRule;

import java.util.Optional;

public interface TariffRuleService extends ISearchService<TariffRule, TariffRuleCondition> {
    Optional<TariffRule> getById(@NonNull Long id);
    TariffRule create(@NonNull TariffRule entity);
    boolean delete(Long id);
}
