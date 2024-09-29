package ru.draen.hps.datagen.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.datagen.controller.dto.CdrGenerationRequest;
import ru.draen.hps.datagen.controller.dto.CdrGenerationResponse;
import ru.draen.hps.datagen.controller.dto.ClientGenerationRequest;
import ru.draen.hps.datagen.controller.dto.ClientGenerationResponse;
import ru.draen.hps.datagen.service.DatagenService;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(value = "/datagen", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DatagenController {
    public static final Long DEFAULT_GENERATION_COUNT = 50L;
    private final DatagenService datagenService;

    @PostMapping("/clients")
    public ResponseEntity<ClientGenerationResponse> generateClients(@RequestBody ClientGenerationRequest request) {
        if (isNull(request.getCount())) request.setCount(DEFAULT_GENERATION_COUNT);
        return ResponseEntity.ok(new ClientGenerationResponse(datagenService.generateClients(request)));
    }

    @PostMapping("/cdr")
    public ResponseEntity<CdrGenerationResponse> generateCdr(@RequestBody CdrGenerationRequest request) {
        if (isNull(request.getCallsCount())) request.setCallsCount(DEFAULT_GENERATION_COUNT);
        return ResponseEntity.ok(datagenService.generateCdr(request));
    }
}
