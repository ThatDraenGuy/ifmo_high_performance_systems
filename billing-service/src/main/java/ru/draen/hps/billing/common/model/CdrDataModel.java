package ru.draen.hps.billing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.draen.hps.common.dbms.domain.ECallDirection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrDataModel {
    private Long cdrDataId;
    private Long cdrFileId;
    private Long clientId;
    private Long reportId;
    private ECallDirection direction;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    @With
    private Integer minutes;
    @With
    private BigDecimal cost;

    public CdrDataModel addToCost(BigDecimal cost) {
        return withCost(isNull(this.cost) ? cost : this.cost.add(cost));
    }
}
