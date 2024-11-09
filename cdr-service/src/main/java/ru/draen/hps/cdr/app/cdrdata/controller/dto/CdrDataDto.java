package ru.draen.hps.cdr.app.cdrdata.controller.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.draen.hps.cdr.app.cdrdata.controller.dto.groups.Processed;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;
import ru.draen.hps.common.r2dbcdao.domain.ECallDirection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrDataDto {
    @NotNull(groups = Processed.class)
    private Long cdrDataId;

    @With
    private Long cdrFileId;

//    @NotNull(groups = Processed.class)
    private Long reportId;

    @NotNull(groups = Processed.class)
    private Long clientId;

    @NotNull(groups = Processed.class)
    private ECallDirection direction;

    @NotNull(groups = Processed.class)
    private OffsetDateTime startTime;

    @NotNull(groups = Processed.class)
    private OffsetDateTime endTime;

    @NotNull(groups = Processed.class)
    private Integer minutes;

    @NotNull(groups = Processed.class)
    private BigDecimal cost;

    public static CdrDataDto of(@Nullable CdrData entity) {
        if (isNull(entity)) return null;
        return new CdrDataDto(
                entity.getId(), entity.getCdrFileId(), entity.getReportId(), entity.getClientId(), entity.getDirection(),
                TimestampHelper.atOffset(entity.getStartTime()), TimestampHelper.atOffset(entity.getEndTime()),
                entity.getMinutes(), entity.getCost()
        );
    }
}
