package ru.draen.hps.app.operator.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.draen.hps.app.operator.controller.dto.OperatorCondition;
import ru.draen.hps.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.app.operator.service.OperatorService;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.domain.Operator;

@RestController
@RequestMapping(value = "/operators", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OperatorController {
    private final OperatorService operatorService;
    private final IMapper<Operator, OperatorDto> operatorMapper;

    @GetMapping("/paged")
    public PageResponse<OperatorDto> findPage(@Validated OperatorCondition condition, PageCondition pageCondition) {
        return operatorService.findAll(condition, pageCondition).map(operatorMapper::toDto);
    }

    @GetMapping
    public ScrollResponse<OperatorDto> find(@Validated OperatorCondition condition, ScrollCondition scrollCondition) {
        return operatorService.findAll(condition, scrollCondition).map(operatorMapper::toDto);
    }

    @PostMapping("/")
    public ResponseEntity<OperatorDto> create(@RequestBody @Validated(Create.class) OperatorDto dto) {
        return ResponseEntity.ok(
                operatorMapper.toDto(
                        operatorService.create(operatorMapper.toEntity(dto))));
    }
}
