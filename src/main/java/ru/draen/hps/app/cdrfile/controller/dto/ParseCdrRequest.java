package ru.draen.hps.app.cdrfile.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParseCdrRequest {
    @NotNull
    private Long fileId;
}
