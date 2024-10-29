package ru.draen.hps.billing.service;

import reactor.core.publisher.Mono;
import ru.draen.hps.billing.controller.dto.BillingRequest;

public interface BillingService {
    Mono<Void> perform(BillingRequest request);
}
