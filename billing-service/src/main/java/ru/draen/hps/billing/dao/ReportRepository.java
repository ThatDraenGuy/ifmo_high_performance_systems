package ru.draen.hps.billing.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.r2dbcdao.domain.Report;

@Repository
public interface ReportRepository extends ReactiveCrudRepository<Report, Long> {
}
