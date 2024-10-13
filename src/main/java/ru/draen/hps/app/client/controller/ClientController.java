package ru.draen.hps.app.client.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.app.client.controller.dto.ClientCondition;
import ru.draen.hps.app.client.controller.dto.ClientDto;
import ru.draen.hps.app.client.service.ClientService;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;

@RestController
@RequestMapping(value = "${api.prefix}/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/paged")
    public PageResponse<ClientDto> findPage(@Validated ClientCondition condition, PageCondition pageCondition) {
        return clientService.findAll(condition, pageCondition).map(ClientDto::of);
    }

    @GetMapping
    public ScrollResponse<ClientDto> find(@Validated ClientCondition condition, ScrollCondition scrollCondition) {
        return clientService.findAll(condition, scrollCondition).map(ClientDto::of);
    }
}
