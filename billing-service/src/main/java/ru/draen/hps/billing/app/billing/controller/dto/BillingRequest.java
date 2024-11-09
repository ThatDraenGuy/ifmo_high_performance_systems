package ru.draen.hps.billing.app.billing.controller.dto;

import jakarta.validation.constraints.NotNull;

public record BillingRequest(
        @NotNull Long cdrFileId
) {
}
