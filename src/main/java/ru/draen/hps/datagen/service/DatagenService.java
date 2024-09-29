package ru.draen.hps.datagen.service;

import ru.draen.hps.datagen.controller.dto.CdrGenerationRequest;
import ru.draen.hps.datagen.controller.dto.CdrGenerationResponse;
import ru.draen.hps.datagen.controller.dto.ClientGenerationRequest;

public interface DatagenService {
    Long generateClients(ClientGenerationRequest request);
    CdrGenerationResponse generateCdr(CdrGenerationRequest request);
}
