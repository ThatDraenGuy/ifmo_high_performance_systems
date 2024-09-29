package ru.draen.hps.datagen.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientGenerationRequest {
    @NotNull
    private Long count;

    @NotNull
    private Long operatorId;
}
