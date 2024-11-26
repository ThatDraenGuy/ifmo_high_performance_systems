package ru.draen.hps.account.app.operator.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.account.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.account.app.operator.service.OperatorService;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.Operator;

@RestController
@RequestMapping(value = "${api.prefix}/operators", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class OperatorController {
    private final OperatorService operatorService;
    private final IMapper<Operator, OperatorDto> operatorMapper;

    @GetMapping("/{id}")
    public ResponseEntity<OperatorDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.of(operatorService.getById(id).map(operatorMapper::toDto));
    }
}
