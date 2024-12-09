package ru.draen.hps.billing.app.report.service;

import reactor.core.publisher.Flux;
import ru.draen.hps.common.dbms.domain.Report;

public interface ReportService {
    Flux<Report> save(Flux<Report> entity);
}
