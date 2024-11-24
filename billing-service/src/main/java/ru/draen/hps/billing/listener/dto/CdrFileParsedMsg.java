package ru.draen.hps.billing.listener.dto;

import ru.draen.hps.common.messaging.model.AppMessage;

public record CdrFileParsedMsg(Long fileId) implements AppMessage {
}
