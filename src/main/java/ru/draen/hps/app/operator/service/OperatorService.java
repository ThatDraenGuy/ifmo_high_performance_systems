package ru.draen.hps.app.operator.service;

import ru.draen.hps.app.operator.controller.dto.OperatorCondition;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.Operator;

public interface OperatorService extends ISearchService<Operator, OperatorCondition> {
}
