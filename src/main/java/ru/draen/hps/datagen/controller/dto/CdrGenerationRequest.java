package ru.draen.hps.datagen.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrGenerationRequest {
    @NotNull
    private Long operatorId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Long callsCount;
}
