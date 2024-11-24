package ru.draen.hps.cdr.producer.dto;

import ru.draen.hps.common.messaging.model.AppMessage;

public record CdrFileParsedMsg(Long fileId) implements AppMessage {
}
