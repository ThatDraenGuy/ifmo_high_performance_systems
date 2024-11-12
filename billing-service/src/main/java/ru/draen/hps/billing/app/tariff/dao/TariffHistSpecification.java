package ru.draen.hps.billing.app.tariff.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.billing.app.tariff.controller.dto.TariffCondition;
import ru.draen.hps.common.dbms.domain.*;
import ru.draen.hps.common.jpadao.utils.EMatchMode;
import ru.draen.hps.common.jpadao.utils.PredicateBuilder;

import java.time.Instant;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TariffHistSpecification {
    public static Specification<TariffHist> logicalKey(TariffHist entity) {
        return !isNull(entity.getTariff().getId()) ? logicalKey(entity.getTariff().getId()) : (root, cq, cb) -> cb.disjunction();
    }

    public static Specification<TariffHist> logicalKey(long tariffId) {
        return (root, cq, cb) -> cb.equal(root.get(TariffHist_.tariff).get(Tariff_.id), tariffId);
    }

    public static Specification<TariffHist> byCondition(TariffCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.operatorId(), operId ->
                        cb.equal(root.get(TariffHist_.tariff).get(Tariff_.operator).get(Operator_.id), operId))
                .addLike(condition.name(), root.get(TariffHist_.tariff).get(Tariff_.name), EMatchMode.ANYWHERE, true)
                .addActual(condition.actualDate(), root)
                .addStatus(condition.status(), root)
                .notDeletedOrReversed(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }

    public static Specification<TariffHist> byTariff(Long tariffId, Instant actualDate) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .add(() -> cb.equal(root.get(TariffHist_.tariff).get(Tariff_.id), tariffId))
                .addActual(actualDate, root)
                .notDeletedOrReversed(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
