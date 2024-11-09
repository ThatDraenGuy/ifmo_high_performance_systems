package ru.draen.hps.billing.app.tariff.dao;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.dbms.domain.Tariff;
import ru.draen.hps.common.jpadao.dao.ISearchRepository;

public interface TariffRepository extends ISearchRepository<Tariff, Long> {
    Tariff save(@NonNull Tariff tariff);
    long count(@NonNull Specification<Tariff> spec);
}
