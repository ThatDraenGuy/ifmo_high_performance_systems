package ru.draen.hps.app.cdrdata.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.domain.ECallDirection;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrDataCondition {
    private Long cdrFileId;
    private ECallDirection direction;
    private Long clientId;
    private Integer minutesMin;
    private Integer minutesMax;
    private BigDecimal costMin;
    private BigDecimal costMax;
}
