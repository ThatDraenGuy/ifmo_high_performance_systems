package ru.draen.hps.file.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadLocalFileRequest(
        @NotBlank
        String path,

        @NotNull
        Long operatorId
) {
}
