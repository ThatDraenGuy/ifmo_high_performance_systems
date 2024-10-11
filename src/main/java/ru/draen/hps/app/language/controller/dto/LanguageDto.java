package ru.draen.hps.app.language.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.domain.Language;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    private Long languageId;
    private String code;
    private String name;

    public static LanguageDto of(Language entity) {
        if (isNull(entity)) return null;
        return new LanguageDto(entity.getId(), entity.getCode(), entity.getName());
    }
}
