package ru.draen.hps.cdr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.Client;
import ru.draen.hps.common.r2dbcdao.domain.Operator;

@FeignClient
public interface DomainClient {
    @GetMapping("/operators/{id}")
    Mono<Operator> findOperatorById(@PathVariable("id") Long id);

    @GetMapping("/clients")
    Mono<Client> findClient(String phoneNumber);
}
