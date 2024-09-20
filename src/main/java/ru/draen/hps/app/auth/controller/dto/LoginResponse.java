package ru.draen.hps.app.auth.controller.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String username
) {
}
