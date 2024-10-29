package ru.draen.hps.billing.dao;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.draen.hps.common.r2dbcdao.domain.TariffRule;

@Repository
public interface TariffRuleRepository extends ReactiveSortingRepository<TariffRule, Long> {
    Flux<TariffRule> findOrderedRules(Long tariffHistId);
}
