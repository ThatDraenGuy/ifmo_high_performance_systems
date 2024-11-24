package ru.draen.hps.notification.model;

import ru.draen.hps.common.messaging.model.AppMessage;

public record FileUploadedMsg(Long fileId) implements AppMessage {
}
