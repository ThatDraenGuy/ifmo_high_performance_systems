package ru.draen.hps.app.tariffrule.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.TariffRule;
import ru.draen.hps.domain.TariffRule_;

@Repository
public class TariffRuleRepositoryImpl extends ADeletableRepository<TariffRule, Long> implements TariffRuleRepository {
    @Override
    protected @NonNull Class<TariffRule> getEntityClass() {
        return TariffRule.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<TariffRule> root) {
        root.fetch(TariffRule_.operator);
    }
}
