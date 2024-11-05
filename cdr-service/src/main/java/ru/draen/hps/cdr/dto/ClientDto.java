package ru.draen.hps.cdr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long clientId;
    private String phoneNumber;
    private OperatorBriefDto operator;
}
