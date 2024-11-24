package ru.draen.hps.file.producer.dto;

import ru.draen.hps.common.messaging.model.AppMessage;

public record FileUploadedMsg(Long fileId) implements AppMessage {
}
