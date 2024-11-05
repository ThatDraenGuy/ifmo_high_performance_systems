package ru.draen.hps.billing.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.draen.hps.common.r2dbcdao.domain.TariffRule;

@Repository
public interface TariffRuleRepository extends ReactiveSortingRepository<TariffRule, Long> {
    @Query("""
    SELECT * FROM tariff_rules trfrl
    INNER JOIN tariff_to_rules map ON trfrl.trfrl_id = map.trfrl_trfrl_id
    WHERE map.trffh_trffh_id = :tariffHistId
    ORDER BY map.rule_ordinal;
    """)
    Flux<TariffRule> findOrderedRules(Long tariffHistId);
}
