package ru.draen.hps.app.tariff.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.ConvertGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.operator.controller.dto.OperatorBriefDto;
import ru.draen.hps.common.entity.EHistStatus;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.common.validation.groups.Pk;
import ru.draen.hps.common.validation.groups.Update;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffDto {
    @Null
    private Long histId;

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long tariffId;

    @Null(groups = Update.class)
    @NotNull(groups = Create.class)
    @Valid
    @ConvertGroup(to = Pk.class)
    private OperatorBriefDto operator;

    @Null(groups = Update.class)
    @NotBlank(groups = Create.class)
    private String name;

    @NotEmpty
    @Valid
    @ConvertGroup.List({
            @ConvertGroup(to = Pk.class),
            @ConvertGroup(from = Create.class, to = Pk.class),
            @ConvertGroup(from = Update.class, to = Pk.class)
    })
    private List<AppliedTariffRuleDto> rules;

    @NotNull
    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    @Null
    private EHistStatus status;
}
