package ru.draen.hps.cdr.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.service.CdrDataService;
import ru.draen.hps.cdr.controller.dto.ParseCdrRequest;
import ru.draen.hps.cdr.service.CdrFileService;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;
import ru.draen.hps.common.r2dbcdao.domain.CdrFile;
import ru.draen.hps.common.r2dbcdao.domain.Client;

@RestController
@RequestMapping(value = "${api.prefix}/cdr-files", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CdrFileController {
    private final CdrFileService cdrFileService;
    private final CdrDataService cdrDataService;

    @PostMapping("/parse")
    public Mono<CdrFile> parse(@Validated @RequestBody ParseCdrRequest request) {
        return cdrFileService.parseData(request.getFileId());
    }

    @GetMapping("/{id}")
    public Mono<CdrFile> findById(@PathVariable("id") Long id) {
        return cdrFileService.findById(id);
    }

    @GetMapping("/{id}/records")
    public Flux<CdrData> findRecords(@PathVariable("id") Long id, Long clientId) {
        return cdrFileService.findById(id).flatMapMany(file -> cdrDataService.findByClient(file.getId(), clientId));
    }
}
