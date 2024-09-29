package ru.draen.hps.app.operator.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.validation.groups.Pk;
import ru.draen.hps.domain.Operator;

import static java.util.Objects.isNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorDto {
    @NotNull(groups = Pk.class)
    private Long operatorId;

    private String name;

    private String code;

    public static OperatorDto of(Operator operator) {
        if (isNull(operator))
            return null;
        return new OperatorDto(operator.getId(), operator.getName(), operator.getCode());
    }
}
