package ru.draen.hps.app.tariffrule.controller.dto;

import jakarta.validation.constraints.NotNull;

public record TariffRuleCondition(
    @NotNull Long operatorId,
    String name
) {
}
