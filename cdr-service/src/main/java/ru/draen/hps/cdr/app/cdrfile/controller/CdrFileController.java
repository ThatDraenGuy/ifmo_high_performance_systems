package ru.draen.hps.cdr.app.cdrfile.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.app.cdrdata.controller.dto.CdrDataDto;
import ru.draen.hps.cdr.app.cdrdata.controller.dto.groups.Processed;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.cdr.app.cdrdata.service.CdrDataService;
import ru.draen.hps.cdr.app.cdrfile.controller.dto.ParseCdrRequest;
import ru.draen.hps.cdr.app.cdrfile.service.CdrFileService;
import ru.draen.hps.cdr.common.model.ClientModel;
import ru.draen.hps.cdr.producer.CdrFileParsedProducer;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;

@RestController
@RequestMapping(value = "${api.prefix}/cdr-files", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CdrFileController {
    private final CdrFileService cdrFileService;
    private final CdrFileParsedProducer cdrFileParsedProducer;
    private final CdrDataService cdrDataService;
    private final IMapper<CdrData, CdrDataDto> cdrDataMapper;

    @PostMapping("/parse")
    public Mono<CdrFileDto> parse(@Validated @RequestBody ParseCdrRequest request) {
        return cdrFileService.parseData(request.getFileId()).flatMap(cdrFileParsedProducer::send);
    }

    @GetMapping("/{id}")
    public Mono<CdrFileDto> findById(@PathVariable("id") Long id) {
        return cdrFileService.findById(id).switchIfEmpty(Mono.error(NotFoundException::new));
    }

    @GetMapping("/{id}/records")
    public Flux<CdrDataDto> findRecords(@PathVariable("id") Long id, @RequestParam("clientId") Long clientId) {
        return cdrDataService.findByClient(id, clientId)
                .map(cdrDataMapper::toDto);
    }

    @GetMapping("/{id}/clients")
    public Flux<ClientModel> findClients(@PathVariable("id") Long id) {
        return cdrDataService.findClients(id);
    }

    @PutMapping("/{id}")
    public Mono<CdrDataDto> updateRecord(@PathVariable("id") Long id,
                                         @Validated(Processed.class) @RequestBody Mono<CdrDataDto> record) {
        return cdrDataService.update(record
                .map(rec -> rec.withCdrFileId(id))
                .map(cdrDataMapper::toEntity))
                .map(cdrDataMapper::toDto);
    }
}
