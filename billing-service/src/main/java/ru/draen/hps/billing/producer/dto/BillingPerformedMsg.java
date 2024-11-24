package ru.draen.hps.billing.producer.dto;

import ru.draen.hps.common.messaging.model.AppMessage;

public record BillingPerformedMsg(Long fileId) implements AppMessage {
}
