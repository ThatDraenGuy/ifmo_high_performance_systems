package ru.draen.hps.account.app.client.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.account.app.client.controller.dto.ClientCondition;
import ru.draen.hps.account.app.client.controller.dto.ClientDto;
import ru.draen.hps.account.app.client.service.ClientService;

@RestController
@RequestMapping(value = "${api.prefix}/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/single")
    public ResponseEntity<ClientDto> find(@Validated ClientCondition condition) {
        return ResponseEntity.of(clientService.findOne(condition).map(ClientDto::of));
    }
}
