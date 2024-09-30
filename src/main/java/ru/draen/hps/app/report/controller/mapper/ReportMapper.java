package ru.draen.hps.app.report.controller.mapper;

import ru.draen.hps.app.client.controller.dto.ClientDto;
import ru.draen.hps.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.app.report.controller.dto.ReportDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.exception.NotImplementedException;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.utils.TimestampHelper;
import ru.draen.hps.domain.Report;

@Mapper
public class ReportMapper implements IMapper<Report, ReportDto> {
    @Override
    public Report toEntity(ReportDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public ReportDto toDto(Report entity) {
        ReportDto dto = new ReportDto();
        dto.setReportId(entity.getId());
        dto.setOperator(OperatorDto.of(entity.getOperator()));
        dto.setClient(ClientDto.of(entity.getClient()));
        dto.setTotalCost(entity.getTotalCost());
        dto.setTotalMinutes(entity.getTotalMinutes());
        dto.setPeriodStartTime(TimestampHelper.atOffset(entity.getStartTime()));
        dto.setPeriodEndTime(TimestampHelper.atOffset(entity.getEndTime()));
        return dto;
    }

    @Override
    public ReportDto toId(Report entity) {
        ReportDto dto = new ReportDto();
        dto.setReportId(entity.getId());
        return dto;
    }
}
