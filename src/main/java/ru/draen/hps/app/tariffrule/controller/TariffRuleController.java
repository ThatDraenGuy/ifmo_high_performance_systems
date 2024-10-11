package ru.draen.hps.app.tariffrule.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleCondition;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleDto;
import ru.draen.hps.app.tariffrule.service.TariffRuleService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.domain.TariffRule;

@RestController
@RequestMapping(value = "/tariff-rules", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TariffRuleController {
    private final TariffRuleService tariffRuleService;
    private final IMapper<TariffRule, TariffRuleDto> tariffRuleMapper;

    @GetMapping("/paged")
    public PageResponse<TariffRuleDto> findPage(@Validated TariffRuleCondition condition, PageCondition pageCondition) {
        return tariffRuleService.findAll(condition, pageCondition).map(tariffRuleMapper::toDto);
    }

    @GetMapping
    public ScrollResponse<TariffRuleDto> find(@Validated TariffRuleCondition condition, ScrollCondition scrollCondition) {
        return tariffRuleService.findAll(condition, scrollCondition).map(tariffRuleMapper::toDto);
    }

    @PostMapping("/")
    public ResponseEntity<TariffRuleDto> create(@RequestBody @Validated(Create.class) TariffRuleDto dto) {
        return ResponseEntity.ok(
                tariffRuleMapper.toDto(
                        tariffRuleService.create(tariffRuleMapper.toEntity(dto)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return tariffRuleService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
