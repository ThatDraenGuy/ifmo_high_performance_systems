package ru.draen.hps.common.r2dbcdao.model;

import org.springframework.data.relational.core.query.CriteriaDefinition;

public interface Specification {
    CriteriaDefinition toPredicate();
}
