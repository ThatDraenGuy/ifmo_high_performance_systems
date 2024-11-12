package ru.draen.hps.account.app.client.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientCondition(
        @NotBlank
        @Pattern(regexp = "^[0-9]+$")
        String phoneNumber
) {
}
