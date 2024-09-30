package ru.draen.hps.app.tariffrule.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleCondition;
import ru.draen.hps.common.utils.EMatchMode;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.Operator_;
import ru.draen.hps.domain.TariffRule;
import ru.draen.hps.domain.TariffRule_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TariffRuleSpecification {
    public static Specification<TariffRule> byCondition(TariffRuleCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.operatorId(), operId -> cb.equal(root.get(TariffRule_.operator).get(Operator_.id), operId))
                .addLike(condition.name(), root.get(TariffRule_.name), EMatchMode.ANYWHERE, true)
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
