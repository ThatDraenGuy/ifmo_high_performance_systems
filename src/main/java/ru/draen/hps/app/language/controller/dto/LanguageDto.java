package ru.draen.hps.app.language.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.validation.groups.Pk;
import ru.draen.hps.domain.Language;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    @NotNull(groups = Pk.class)
    private Long languageId;
    private String code;
    private String name;

    public static LanguageDto of(Language entity) {
        if (isNull(entity)) return null;
        return new LanguageDto(entity.getId(), entity.getCode(), entity.getName());
    }
}
