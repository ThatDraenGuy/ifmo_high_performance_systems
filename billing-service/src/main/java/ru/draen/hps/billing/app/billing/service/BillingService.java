package ru.draen.hps.billing.app.billing.service;


import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;

public interface BillingService {
    Mono<Void> perform(BillingRequest request);
}
