package ru.draen.hps.app.report.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.report.controller.dto.ReportCondition;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.Client_;
import ru.draen.hps.domain.Operator_;
import ru.draen.hps.domain.Report;
import ru.draen.hps.domain.Report_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportSpecification {
    public static Specification<Report> byCondition(ReportCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.operatorId(), operId -> cb.equal(root.get(Report_.operator).get(Operator_.id), operId))
                .addIfNotNull(condition.clientId(), cliId -> cb.equal(root.get(Report_.client).get(Client_.id), cliId))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
