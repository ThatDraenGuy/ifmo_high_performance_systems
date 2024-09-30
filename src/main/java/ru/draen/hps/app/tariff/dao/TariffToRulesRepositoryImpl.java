package ru.draen.hps.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.AGenericRepository;
import ru.draen.hps.domain.TariffToRules;
import ru.draen.hps.domain.TariffToRules_;

import java.util.List;

@Repository
public class TariffToRulesRepositoryImpl extends AGenericRepository<TariffToRules, TariffToRules.PK> implements TariffToRulesRepository {
    @Override
    protected @NonNull Class<TariffToRules> getEntityClass() {
        return TariffToRules.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<TariffToRules> root) {
        root.fetch(TariffToRules_.tariffRule);
    }

    @Override
    public void save(List<TariffToRules> rules) {
        rules.forEach(entityManager::persist);
        entityManager.flush();
    }
}
