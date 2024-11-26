package ru.draen.hps.billing.app.billing.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.billing.service.BillingService;
import ru.draen.hps.billing.producer.BillingPerformedProducer;

@RestController
@RequestMapping(value = "${api.prefix}/billing", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class BillingController {
    private final BillingService billingService;
    private final BillingPerformedProducer billingPerformedProducer;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> perform(@RequestBody @Validated BillingRequest request) {
        return billingService.perform(request).flatMap(billingPerformedProducer::send);
    }
}
