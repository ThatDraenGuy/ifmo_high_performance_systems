package ru.draen.hps.file.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.core.validation.groups.Pk;
import ru.draen.hps.common.dbms.domain.Operator;

import static java.util.Objects.isNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorBriefDto {
    @NotNull(groups = Pk.class)
    private Long operatorId;

    private String name;

    private String code;

    public static OperatorBriefDto of(Operator operator) {
        if (isNull(operator))
            return null;
        return new OperatorBriefDto(operator.getId(), operator.getName(), operator.getCode());
    }
}
