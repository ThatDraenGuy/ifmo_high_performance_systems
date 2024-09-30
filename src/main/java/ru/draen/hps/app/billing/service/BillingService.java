package ru.draen.hps.app.billing.service;

import ru.draen.hps.app.billing.controller.dto.BillingRequest;

public interface BillingService {
    void perform(BillingRequest request);
}
