package ru.draen.hps.billing.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.TariffHist;

import java.time.Instant;

@Repository
public interface TariffHistRepository extends ReactiveSortingRepository<TariffHist, Long> {
    @Query("""
        SELECT * FROM tariff_hist th
        WHERE
            th.trff_trff_id = :tariffId
            AND th.start_date <= :actualDate
            AND th.end_date > :actualDate
            AND th.del_date IS NULL;
        """)
    Mono<TariffHist> findByTariff(Long tariffId, Instant actualDate);
}
