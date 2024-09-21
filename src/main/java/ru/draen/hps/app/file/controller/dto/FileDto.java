package ru.draen.hps.app.file.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.validation.groups.Create;

@Getter
@Setter
public class FileDto {
    @Null(groups = Create.class)
    private Long fileId;

    @NotBlank
    private String fileName;

    @NotNull
    private Long operatorId;

    @NotNull
    @Valid
    private FileContentDto content;
}
