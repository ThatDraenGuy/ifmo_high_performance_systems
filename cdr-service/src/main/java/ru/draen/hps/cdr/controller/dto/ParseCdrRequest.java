package ru.draen.hps.cdr.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParseCdrRequest {
    @NotNull
    private Long fileId;
}
