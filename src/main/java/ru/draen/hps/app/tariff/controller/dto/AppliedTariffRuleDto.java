package ru.draen.hps.app.tariff.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppliedTariffRuleDto {
    @NotNull
    @Valid
    private TariffRuleDto rule;

    @NotNull
    private Integer ordinal;
}
