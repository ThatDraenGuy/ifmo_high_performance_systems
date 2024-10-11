package ru.draen.hps.app.tariff.dao;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.Tariff;

public interface TariffRepository extends ISearchRepository<Tariff, Long> {
    Tariff save(@NonNull Tariff tariff);
    long count(@NonNull Specification<Tariff> spec);
}
