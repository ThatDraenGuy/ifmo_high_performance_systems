package ru.draen.hps.app.report.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.app.report.controller.dto.ReportCondition;
import ru.draen.hps.app.report.controller.dto.ReportDto;
import ru.draen.hps.app.report.service.ReportService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.Report;

@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final IMapper<Report, ReportDto> reportMapper;

    @GetMapping("/paged")
    public PageResponse<ReportDto> findPage(@Validated ReportCondition condition, PageCondition pageCondition) {
        return reportService.findAll(condition, pageCondition).map(reportMapper::toDto);
    }

    @GetMapping()
    public ScrollResponse<ReportDto> find(@Validated ReportCondition condition, ScrollCondition scrollCondition) {
        return reportService.findAll(condition, scrollCondition).map(reportMapper::toDto);
    }
}
