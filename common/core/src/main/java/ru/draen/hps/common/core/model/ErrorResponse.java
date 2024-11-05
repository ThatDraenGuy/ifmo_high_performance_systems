package ru.draen.hps.common.core.model;

public record ErrorResponse(
        int errorCode,
        String userMsg,
        String devMsg,
        String path
) {
}
