package ru.draen.hps.billing.app.tariff.service;

import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.dbms.domain.Tariff;
import ru.draen.hps.common.dbms.domain.TariffHist;

import java.util.Optional;

public interface TariffService {
    Optional<Tariff> findById(Long id);
    Mono<TariffHist> findOne(Specification<TariffHist> spec);
}
