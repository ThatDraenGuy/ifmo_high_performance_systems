package ru.draen.hps.common.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ru.draen.hps.common.entity.ADeletableEntity;
import ru.draen.hps.common.entity.ADeletableEntity_;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

@AllArgsConstructor
public class PredicateBuilder {
    private static final char SQL_LIKE_ESCAPE_CHAR = '\\';
    private static final Pattern CHARS_TO_ESCAPE_PATTERN = Pattern.compile("[_%]" + '\\' + SQL_LIKE_ESCAPE_CHAR + "]");

    private final CriteriaBuilder cb;
    private final List<Predicate> predicates = new ArrayList<>();

    public <T> PredicateBuilder addIfNotNull(T item, @NonNull Function<T, Predicate> func) {
        if (item != null) {
            this.predicates.add(func.apply(item));
        }
        return this;
    }

    public <T> PredicateBuilder addIfNotNull(Collection<T> collection, @NonNull Function<Collection<T>, Predicate> func) {
        if (CollectionUtils.isNotEmpty(collection)) {
            predicates.add(func.apply(collection));
        }
        return this;
    }

    public PredicateBuilder addLike(String item, Expression<String> path, EMatchMode matchMode, boolean ignoreCase) {
        if (StringUtils.isNotBlank(item)) {
            predicates.add(cb.like(
                    ignoreCase ? cb.upper(path) : path,
                    matchMode.toMatchString(escape(ignoreCase ? item.trim().toUpperCase() : item.trim()))
            ));
        }
        return this;
    }

    public <T extends ADeletableEntity<?>> PredicateBuilder notDeleted(@NonNull Path<T> deletePath) {
        predicates.add(
                cb.isNull(deletePath.get(ADeletableEntity_.delDate))
        );
        return this;
    }

    public Predicate toPredicate(@NonNull EPredicateMode mode) {
        if (predicates.isEmpty()) {
            return cb.conjunction();
        }

        if (predicates.size() == 1) {
            return predicates.getFirst();
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        return switch (mode) {
            case OR -> cb.or(predicateArray);
            case AND -> cb.and(predicateArray);
        };
    }

    private String escape(String value) {
        return CHARS_TO_ESCAPE_PATTERN
                .matcher(value)
                .replaceAll('\\' + SQL_LIKE_ESCAPE_CHAR + "$0");
    }

    public enum EPredicateMode {
        AND,
        OR;
    }
}
