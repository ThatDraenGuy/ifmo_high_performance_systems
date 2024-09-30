package ru.draen.hps.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.Tariff;

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
