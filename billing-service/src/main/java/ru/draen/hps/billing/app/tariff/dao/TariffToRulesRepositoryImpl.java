package ru.draen.hps.billing.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dbms.domain.TariffToRules;
import ru.draen.hps.common.dbms.domain.TariffToRules_;
import ru.draen.hps.common.jpadao.dao.AGenericRepository;

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
