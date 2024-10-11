package ru.draen.hps.app.cdrdata.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.cdrdata.controller.dto.CdrDataCondition;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CdrDataSpecification {
    public static Specification<CdrData> byClient(@NonNull CdrFile cdrFile, @NonNull Client client) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .add(() -> cb.equal(root.get(CdrData_.cdrFile), cdrFile))
                .add(() -> cb.equal(root.get(CdrData_.client), client))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }

    public static Specification<CdrData> byCondition(@NonNull CdrDataCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.getCdrFileId(), cdrFileId -> cb.equal(root.get(CdrData_.cdrFile).get(CdrFile_.id), cdrFileId))
                .addIfNotNull(condition.getDirection(), dir -> cb.equal(root.get(CdrData_.direction), dir))
                .addIfNotNull(condition.getClientId(), cliId -> cb.equal(root.get(CdrData_.client).get(Client_.id), cliId))
                .addIfNotNull(condition.getMinutesMin(), min -> cb.greaterThanOrEqualTo(root.get(CdrData_.minutes), min))
                .addIfNotNull(condition.getMinutesMax(), max -> cb.lessThanOrEqualTo(root.get(CdrData_.minutes), max))
                .addIfNotNull(condition.getCostMin(), min -> cb.greaterThanOrEqualTo(root.get(CdrData_.cost), min))
                .addIfNotNull(condition.getCostMax(), max -> cb.lessThanOrEqualTo(root.get(CdrData_.cost), max))
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
