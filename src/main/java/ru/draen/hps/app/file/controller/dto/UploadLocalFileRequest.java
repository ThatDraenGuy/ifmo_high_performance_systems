package ru.draen.hps.app.file.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadLocalFileRequest(
        @NotBlank
        String path,

        @NotNull
        Long operatorId
) {
}
