package ru.draen.hps.notification.model;

import ru.draen.hps.common.messaging.model.AppMessage;

public record BillingPerformedMsg(Long fileId) implements AppMessage {
}
