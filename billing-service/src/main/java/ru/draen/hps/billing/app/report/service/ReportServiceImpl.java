package ru.draen.hps.billing.app.report.service;

import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.report.dao.ReportRepository;
import ru.draen.hps.common.dbms.domain.Report;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final TransactionTemplate transactionTemplate;
    private final ReportRepository reportRepository;

    @Override
    public Flux<Report> save(Flux<Report> entities) {
        return entities.map(entity -> transactionTemplate.execute(status -> reportRepository.save(entity)));
    }
}
