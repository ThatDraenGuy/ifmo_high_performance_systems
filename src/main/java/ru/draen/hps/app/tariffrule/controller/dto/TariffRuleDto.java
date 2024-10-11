package ru.draen.hps.app.tariffrule.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.ConvertGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.operator.controller.dto.OperatorBriefDto;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.common.validation.groups.Pk;
import ru.draen.hps.common.validation.groups.Update;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffRuleDto {
    @Null(groups = {Create.class})
    @NotNull(groups = {Update.class, Pk.class})
    private Long ruleId;

    @Null(groups = Pk.class)
    @NotNull
    @Valid
    @ConvertGroup(to = Pk.class)
    private OperatorBriefDto operator;

    @Null(groups = Pk.class)
    @NotBlank
    private String name;

    @Null(groups = Pk.class)
    @NotNull
    @Min(0)
    private BigDecimal minuteCost;

    @Null(groups = Pk.class)
    @Min(0)
    private Integer minuteLimit;
}
