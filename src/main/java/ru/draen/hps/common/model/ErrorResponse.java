package ru.draen.hps.common.model;

public record ErrorResponse(
        int errorCode,
        String userMsg,
        String devMsg
) {
}
