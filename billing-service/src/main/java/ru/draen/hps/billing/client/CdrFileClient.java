package ru.draen.hps.billing.client;

import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.common.model.CdrDataModel;
import ru.draen.hps.billing.common.model.CdrFileModel;
import ru.draen.hps.billing.common.model.ClientModel;
import ru.draen.hps.common.webflux.interceptor.CommonClientConfig;


@ReactiveFeignClient(value = "cdr-service", path = "${api.prefix}/cdr-files", primary = false,
        configuration = CommonClientConfig.class)
public interface CdrFileClient {
    @GetMapping("/{id}")
    Mono<CdrFileModel> findById(@PathVariable("id") Long fileId);

    @GetMapping("/{id}/clients")
    Flux<ClientModel> findClients(@PathVariable("id") Long fileId);

    @GetMapping("/{id}/records")
    Flux<CdrDataModel> findClientRecords(@PathVariable("id") Long fileId, @RequestParam("clientId") Long clientId);

    @PutMapping("/{id}")
    Mono<CdrDataModel> updateRecord(@PathVariable("id") Long fileId, @RequestBody CdrDataModel record);
}
