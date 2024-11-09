package ru.draen.hps.billing.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dbms.domain.Tariff;
import ru.draen.hps.common.jpadao.dao.ADeletableRepository;

@Repository
public class TariffRepositoryImpl extends ADeletableRepository<Tariff, Long> implements TariffRepository {
    @Override
    protected @NonNull Class<Tariff> getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Tariff> root) {
    }
}
