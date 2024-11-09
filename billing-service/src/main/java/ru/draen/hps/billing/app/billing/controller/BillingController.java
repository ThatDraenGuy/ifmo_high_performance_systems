package ru.draen.hps.billing.app.billing.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.billing.service.BillingService;

@RestController
@RequestMapping(value = "${api.prefix}/billing", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @PostMapping("/")
    public Mono<Void> perform(@RequestBody @Validated BillingRequest request) {
        return billingService.perform(request);
    }
}
