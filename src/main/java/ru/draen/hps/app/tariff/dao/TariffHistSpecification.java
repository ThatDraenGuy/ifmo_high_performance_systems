package ru.draen.hps.app.tariff.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.TariffHist;
import ru.draen.hps.domain.TariffHist_;
import ru.draen.hps.domain.Tariff_;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TariffHistSpecification {
    public static Specification<TariffHist> logicalKey(TariffHist entity) {
        return logicalKey(entity.getTariff().getId());
    }

    public static Specification<TariffHist> logicalKey(long tariffId) {
        return (root, cq, cb) -> cb.equal(root.get(TariffHist_.tariff).get(Tariff_.id), tariffId);
    }

    public static Specification<TariffHist> byTariffs(Set<Long> tariffIds) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .add(() -> root.get(TariffHist_.tariff).get(Tariff_.id).in(tariffIds))
                .notDeletedOrReversed(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
