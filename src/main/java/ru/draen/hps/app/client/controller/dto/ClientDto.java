package ru.draen.hps.app.client.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.domain.Client;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long clientId;
    private String phoneNumber;
    private OperatorDto operator;

    public static ClientDto of(Client client) {
        if (isNull(client)) return null;
        return new ClientDto(client.getId(), client.getPhoneNumber(), OperatorDto.of(client.getOperator()));
    }
}
