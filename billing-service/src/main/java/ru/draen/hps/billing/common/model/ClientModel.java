package ru.draen.hps.billing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientModel {
    private Long clientId;
    private String phoneNumber;
    private OperatorBriefModel operator;
    private Long tariffId;
}
