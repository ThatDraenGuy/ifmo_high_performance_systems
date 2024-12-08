package ru.draen.hps.billing.app.billing.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.common.webflux.saga.SagaStep;

@RestController
@RequestMapping(value = "${api.prefix}/billing", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class BillingController {
    private final SagaStep<BillingRequest, Void> billingSagaStep;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> perform(@RequestBody @Validated BillingRequest request) {
        return billingSagaStep.process(request);
    }
}
