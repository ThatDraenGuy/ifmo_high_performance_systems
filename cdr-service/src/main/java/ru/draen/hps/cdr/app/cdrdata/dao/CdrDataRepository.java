package ru.draen.hps.cdr.app.cdrdata.dao;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;

import java.time.Instant;

@Repository
public interface CdrDataRepository extends ReactiveCrudRepository<CdrData, Long> {
    @Query("""
    SELECT * FROM cdr_data cdrd
    WHERE
        cdrd.cdrf_file_id = :cdrFileId
        AND cdrd.cli_cli_id = :clientId
        AND cdrd.rprt_rprt_id IS NULL
        AND cdrd.del_date IS NULL
    ORDER BY cdrd.start_time;
    """)
    Flux<CdrData> findByClient(Long cdrFileId, Long clientId);

    @Query("""
    SELECT cdrd.cli_cli_id FROM cdr_data cdrd
    WHERE cdrd.cdrf_file_id = :cdrFileId
    AND cdrd.del_date IS NULL;
    """)
    Flux<Long> findClientIds(Long cdrFileId);

    @Modifying
    @Query("""
    UPDATE cdr_data
    SET del_date = :moment
    WHERE cdrf_file_id = :cdrFileId;
    """)
    Mono<Void> deleteByFile(Instant moment, Long cdrFileId);
}
