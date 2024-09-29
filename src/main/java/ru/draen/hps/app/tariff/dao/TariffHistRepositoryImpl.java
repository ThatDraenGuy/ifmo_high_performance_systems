package ru.draen.hps.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.AHistoricalRepository;
import ru.draen.hps.domain.TariffHist;

@Repository
public class TariffHistRepositoryImpl extends AHistoricalRepository<TariffHist, Long> implements TariffHistRepository {
    @Override
    protected @NonNull Class<TariffHist> getEntityClass() {
        return TariffHist.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<TariffHist> root) {

    }

    @Override
    public Specification<TariffHist> logicalKey(@NonNull TariffHist entity) {
        return TariffHistSpecification.logicalKey(entity);
    }
}
