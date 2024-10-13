package ru.draen.hps.app.billing.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.app.billing.service.BillingService;

@RestController
@RequestMapping(value = "${api.prefix}/billing", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @PostMapping("/")
    public ResponseEntity<?> perform(@RequestBody @Validated BillingRequest request) {
        billingService.perform(request);
        return ResponseEntity.noContent().build();
    }
}
