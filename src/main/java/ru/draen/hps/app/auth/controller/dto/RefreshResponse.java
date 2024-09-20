package ru.draen.hps.app.auth.controller.dto;

public record RefreshResponse(
        String accessToken,
        String refreshToken,
        String username
) {
}
