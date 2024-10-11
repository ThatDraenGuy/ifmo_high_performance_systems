package ru.draen.hps.app.report.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.client.controller.dto.ClientDto;
import ru.draen.hps.app.operator.controller.dto.OperatorBriefDto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long reportId;
    private OperatorBriefDto operator;
    private ClientDto client;
    private BigDecimal totalCost;
    private Integer totalMinutes;
    private OffsetDateTime periodStartTime;
    private OffsetDateTime periodEndTime;
}
