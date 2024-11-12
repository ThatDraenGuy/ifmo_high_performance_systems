package ru.draen.hps.file.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.ConvertGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.core.validation.groups.Create;
import ru.draen.hps.common.core.validation.groups.Pk;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    @Null(groups = Create.class)
    @NotNull(groups = Pk.class)
    private Long fileId;

    @NotBlank(groups = Create.class)
    private String fileName;

    @NotNull(groups = Create.class)
    @Valid
    @ConvertGroup(from = Create.class, to = Pk.class)
    private OperatorBriefDto operator;

    @NotNull(groups = Create.class)
    @Valid
    private FileContentDto content;
}
