package ru.draen.hps.app.report.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.Report;

@Repository
public class ReportRepositoryImpl extends ADeletableRepository<Report, Long> implements ReportRepository {
    @Override
    protected @NonNull Class<Report> getEntityClass() {
        return Report.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Report> root) {
    }
}