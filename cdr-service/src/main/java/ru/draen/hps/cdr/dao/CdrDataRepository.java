package ru.draen.hps.cdr.dao;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;

@Repository
public interface CdrDataRepository extends ReactiveCrudRepository<CdrData, Long> {
    @Query("""
    SELECT * FROM cdr_data cdrd
    WHERE
        cdrd.cdrf_file_id = :cdrFileId
        AND cdrd.cli_cli_id = :clientId
    ORDER BY cdrd.start_time;
    """)
    Flux<CdrData> findByClient(Long cdrFileId, Long clientId);
}
