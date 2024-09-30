package ru.draen.hps.app.report.dao;

import lombok.NonNull;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.Report;

public interface ReportRepository extends ISearchRepository<Report, Long> {
    Report save(@NonNull Report report);
    boolean delete(Long id);
}
