package ru.draen.hps.app.file.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.ConvertGroup;
import lombok.Data;
import ru.draen.hps.app.operator.controller.dto.OperatorBriefDto;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.common.validation.groups.Pk;

@Data
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
