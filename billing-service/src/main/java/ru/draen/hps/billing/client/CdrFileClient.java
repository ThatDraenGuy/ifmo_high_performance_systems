package ru.draen.hps.billing.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;
import ru.draen.hps.common.r2dbcdao.domain.CdrFile;
import ru.draen.hps.common.r2dbcdao.domain.Client;


@FeignClient(value = "cdr-service", path = "${api.prefix}/cdr-files")
public interface CdrFileClient {
    @GetMapping("/{id}")
    Mono<CdrFile> findById(@PathVariable("id") Long fileId);

    @GetMapping("/{id}/clients")
    Flux<Client> findClients(@PathVariable("id") Long fileId);

    @GetMapping("/{id}/records")
    Flux<CdrData> findClientRecords(@PathVariable("id") Long fileId, Long clientId);
}
