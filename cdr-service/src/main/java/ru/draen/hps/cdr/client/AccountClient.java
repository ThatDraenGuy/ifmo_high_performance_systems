package ru.draen.hps.cdr.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.dto.ClientDto;
import ru.draen.hps.cdr.dto.OperatorBriefDto;

@ReactiveFeignClient(value = "account-service", path = "${api.prefix}")
public interface AccountClient {
    @GetMapping("/operators/{id}")
    Mono<OperatorBriefDto> findOperatorById(@PathVariable("id") Long id);

    @GetMapping("/clients/single")
    Mono<ClientDto> findClient(@RequestParam("phoneNumber") String phoneNumber);
}
