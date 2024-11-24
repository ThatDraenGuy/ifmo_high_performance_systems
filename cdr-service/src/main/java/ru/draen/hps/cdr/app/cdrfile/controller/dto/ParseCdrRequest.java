package ru.draen.hps.cdr.app.cdrfile.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseCdrRequest {
    @NotNull
    private Long fileId;
}
