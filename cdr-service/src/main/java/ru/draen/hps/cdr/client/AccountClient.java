package ru.draen.hps.cdr.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.common.model.ClientModel;
import ru.draen.hps.cdr.common.model.OperatorBriefModel;

@ReactiveFeignClient(value = "account-service", qualifier = "account-client", path = "${api.prefix}", primary = false,
        configuration = AccountClientConfig.class)
public interface AccountClient {
    @GetMapping("/operators/{id}")
    Mono<OperatorBriefModel> findOperatorById(@PathVariable("id") Long id);

    @GetMapping("/clients/single")
    Mono<ClientModel> findClient(@RequestParam("phoneNumber") String phoneNumber);

    @GetMapping("/clients/{id}")
    Mono<ClientModel> findClientById(@PathVariable("id") Long id);
}
