package ru.draen.hps.app.report.service;

import ru.draen.hps.app.report.controller.dto.ReportCondition;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.Report;

public interface ReportService extends ISearchService<Report, ReportCondition> {
    boolean delete(Long id);
}
