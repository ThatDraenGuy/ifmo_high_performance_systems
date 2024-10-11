package ru.draen.hps.app.operator.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.operator.controller.dto.OperatorCondition;
import ru.draen.hps.common.utils.EMatchMode;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.Operator;
import ru.draen.hps.domain.Operator_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperatorSpecification {
    public static Specification<Operator> byCondition(OperatorCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.operatorId(), operId -> cb.equal(root.get(Operator_.id), operId))
                .addLike(condition.name(), root.get(Operator_.name), EMatchMode.ANYWHERE, true)
                .addLike(condition.code(), root.get(Operator_.code), EMatchMode.ANYWHERE, true)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
