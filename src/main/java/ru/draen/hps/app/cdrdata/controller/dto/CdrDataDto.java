package ru.draen.hps.app.cdrdata.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.client.controller.dto.ClientDto;
import ru.draen.hps.common.utils.TimestampHelper;
import ru.draen.hps.domain.CdrData;
import ru.draen.hps.domain.ECallDirection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrDataDto {
    private Long cdrDataId;
    private ClientDto client;
    private ECallDirection direction;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer minutes;
    private BigDecimal cost;

    public static CdrDataDto of(CdrData entity) {
        if (isNull(entity)) return null;
        return new CdrDataDto(
                entity.getId(),
                ClientDto.of(entity.getClient()),
                entity.getDirection(),
                TimestampHelper.atOffset(entity.getStartTime()),
                TimestampHelper.atOffset(entity.getEndTime()),
                entity.getMinutes(),
                entity.getCost()
        );
    }
}
