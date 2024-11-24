package ru.draen.hps.cdr.listener.dto;

import ru.draen.hps.common.messaging.model.AppMessage;

public record FileUploadedMsg(Long fileId) implements AppMessage {
}
