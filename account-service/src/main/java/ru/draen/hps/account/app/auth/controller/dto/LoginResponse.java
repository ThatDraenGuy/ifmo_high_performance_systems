package ru.draen.hps.account.app.auth.controller.dto;

public record LoginResponse(
        String accessToken,
        String username
) {
}
