package ru.draen.hps.billing.controller.dto;

import jakarta.validation.constraints.NotNull;

public record BillingRequest(
        @NotNull Long cdrFileId
) {
}
