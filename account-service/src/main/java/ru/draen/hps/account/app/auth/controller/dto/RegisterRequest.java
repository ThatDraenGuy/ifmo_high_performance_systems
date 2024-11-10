package ru.draen.hps.account.app.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
