package ru.draen.hps.account.app.operator.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.account.app.language.controller.dto.LanguageDto;
import ru.draen.hps.common.core.validation.groups.Create;
import ru.draen.hps.common.core.validation.groups.Pk;
import ru.draen.hps.common.core.validation.groups.Update;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorDto {
    @Null(groups = Create.class)
    private Long operatorId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(min = 5, max = 5)
    private String code;

    @NotEmpty
    @Valid
    @ConvertGroup.List({
            @ConvertGroup(to = Pk.class),
            @ConvertGroup(from = Create.class, to = Pk.class),
            @ConvertGroup(from = Update.class, to = Pk.class)
    })
    private List<LanguageDto> languages;
}
