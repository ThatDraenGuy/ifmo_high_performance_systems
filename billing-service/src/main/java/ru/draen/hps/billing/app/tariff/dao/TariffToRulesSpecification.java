package ru.draen.hps.billing.app.tariff.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.dbms.domain.TariffHist_;
import ru.draen.hps.common.dbms.domain.TariffToRules;
import ru.draen.hps.common.dbms.domain.TariffToRules_;
import ru.draen.hps.common.dbms.domain.Tariff_;
import ru.draen.hps.common.jpadao.utils.PredicateBuilder;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TariffToRulesSpecification {

    public static Specification<TariffToRules> byTariffs(Set<Long> tariffIds) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .add(() -> root.get(TariffToRules_.tariffHist).get(TariffHist_.tariff).get(Tariff_.id).in(tariffIds))
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
