package ru.draen.hps.cdr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorBriefDto {
    private Long operatorId;
    private String name;
    private String code;
}
