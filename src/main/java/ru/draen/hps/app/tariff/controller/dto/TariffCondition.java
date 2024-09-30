package ru.draen.hps.app.tariff.controller.dto;

import jakarta.validation.constraints.NotNull;
import ru.draen.hps.common.entity.EHistStatus;

import java.time.OffsetDateTime;

public record TariffCondition(
    @NotNull Long operatorId,
    String name,
    OffsetDateTime actualDate,
    EHistStatus status
) {
}
