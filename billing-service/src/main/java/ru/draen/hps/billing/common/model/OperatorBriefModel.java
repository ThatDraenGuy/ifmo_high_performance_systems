package ru.draen.hps.billing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorBriefModel {
    private Long operatorId;
    private String name;
    private String code;
}
