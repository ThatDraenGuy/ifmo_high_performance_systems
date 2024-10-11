package ru.draen.hps.app.operator.dao;

import jakarta.persistence.criteria.From;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.draen.hps.domain.Operator;
import ru.draen.hps.domain.Operator_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperatorFetchProfile {
    public static void all(From<?, Operator> root) {
        root.fetch(Operator_.languages);
    }
}
