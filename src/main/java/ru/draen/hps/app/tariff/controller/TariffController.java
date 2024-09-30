package ru.draen.hps.app.tariff.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.draen.hps.app.tariff.controller.dto.TariffCondition;
import ru.draen.hps.app.tariff.controller.dto.TariffDto;
import ru.draen.hps.app.tariff.service.TariffService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.common.validation.groups.Update;
import ru.draen.hps.domain.TariffHist;

@RestController
@RequestMapping(value = "/tariffs", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TariffController {
    private final TariffService tariffService;
    private final IMapper<TariffHist, TariffDto> tariffMapper;

    @GetMapping("/paged")
    public PageResponse<TariffDto> findPage(@Validated TariffCondition condition, PageCondition pageCondition) {
        return tariffService.findAll(condition, pageCondition).map(tariffMapper::toDto);
    }

    @GetMapping
    public ScrollResponse<TariffDto> find(@Validated TariffCondition condition, ScrollCondition scrollCondition) {
        return tariffService.findAll(condition, scrollCondition).map(tariffMapper::toDto);
    }

    @PostMapping("/")
    public ResponseEntity<TariffDto> create(@RequestBody @Validated(Create.class) TariffDto dto) {
        return ResponseEntity.ok(
                tariffMapper.toDto(
                        tariffService.create(tariffMapper.toEntity(dto))
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffDto> update(@PathVariable(name = "id") Long id, @RequestBody @Validated(Update.class) TariffDto dto) {
        dto.setTariffId(id);
        return ResponseEntity.of(
                tariffService.update(tariffMapper.toEntity(dto)).map(tariffMapper::toDto)
        );
    }
}
