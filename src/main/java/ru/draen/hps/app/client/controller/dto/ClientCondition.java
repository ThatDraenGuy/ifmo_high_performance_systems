package ru.draen.hps.app.client.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientCondition(
        @Pattern(regexp = "^[0-9]+$")
        String phoneNumber,

        @NotNull
        Long operatorId
) {
}
