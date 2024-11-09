package ru.draen.hps.billing.app.report.dao;

import lombok.NonNull;
import ru.draen.hps.common.dbms.domain.Report;
import ru.draen.hps.common.jpadao.dao.ISearchRepository;

public interface ReportRepository extends ISearchRepository<Report, Long> {
    Report save(@NonNull Report report);
    boolean delete(Long id);
}
