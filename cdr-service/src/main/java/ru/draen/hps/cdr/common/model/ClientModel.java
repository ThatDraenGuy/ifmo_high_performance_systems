package ru.draen.hps.cdr.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.core.validation.groups.Pk;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {
    @NotNull(groups = Pk.class)
    private Long clientId;
    private String phoneNumber;
    private OperatorBriefModel operator;
    private Long tariffId;
}
