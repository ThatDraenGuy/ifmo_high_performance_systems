package ru.draen.hps.billing.app.report.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.dbms.domain.Report;

public interface ReportService {
    Flux<Report> save(Flux<Report> entity);
}
