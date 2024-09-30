package ru.draen.hps.app.report.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.report.controller.dto.ReportCondition;
import ru.draen.hps.app.report.dao.ReportRepository;
import ru.draen.hps.app.report.dao.ReportSpecification;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.Report;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final ReportRepository reportRepository;

    @Override
    public boolean delete(Long id) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> reportRepository.delete(id))); //TODO paid/not paid
    }

    @Override
    public PageResponse<Report> findAll(@NonNull ReportCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Report> spec = ReportSpecification.byCondition(condition);
            return new PageResponse<>(
                    reportRepository.findAll(spec, pageCondition),
                    pageCondition,
                    reportRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<Report> findAll(@NonNull ReportCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Report> spec = ReportSpecification.byCondition(condition);
            return new ScrollResponse<>(
                    reportRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }
}
