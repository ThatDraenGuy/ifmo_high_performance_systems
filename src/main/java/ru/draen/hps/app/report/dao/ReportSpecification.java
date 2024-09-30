package ru.draen.hps.app.report.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.report.controller.dto.ReportCondition;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.Report;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportSpecification {
    public static Specification<Report> byCondition(ReportCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                //TODO
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
