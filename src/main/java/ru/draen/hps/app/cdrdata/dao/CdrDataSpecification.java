package ru.draen.hps.app.cdrdata.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.CdrData;
import ru.draen.hps.domain.CdrData_;
import ru.draen.hps.domain.CdrFile;
import ru.draen.hps.domain.Client;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CdrDataSpecification {
    public static Specification<CdrData> byClient(@NonNull CdrFile cdrFile, @NonNull Client client) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .add(() -> cb.equal(root.get(CdrData_.cdrFile), cdrFile))
                .add(() -> cb.equal(root.get(CdrData_.client), client))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
