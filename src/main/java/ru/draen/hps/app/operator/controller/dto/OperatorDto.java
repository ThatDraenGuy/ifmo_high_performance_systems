package ru.draen.hps.app.operator.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.language.controller.dto.LanguageDto;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorDto {
    private Long operatorId;
    private String name;
    private String code;
    private List<LanguageDto> languages;
}
