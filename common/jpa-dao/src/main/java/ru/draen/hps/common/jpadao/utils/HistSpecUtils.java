package ru.draen.hps.common.jpadao.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.draen.hps.common.jpadao.entity.AHistoricalEntity;
import ru.draen.hps.common.jpadao.entity.AHistoricalEntity_;
import ru.draen.hps.common.jpadao.entity.EHistStatus;

import java.time.Instant;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HistSpecUtils {
    public static <T extends AHistoricalEntity<?>> Predicate resolve(EHistStatus status, @NonNull CriteriaBuilder cb,
                                                                    @NonNull Path<T> histPath) {
        if (isNull(status)) return cb.conjunction();
        return switch (status) {
            case ACTIVE -> activeNow(cb, histPath);
            case FUTURE -> futureNow(cb, histPath);
            case OBSOLETE -> obsoleteNow(cb, histPath);
            case DELETED -> deleted(cb, histPath);
        };
    }

    public static <T extends AHistoricalEntity<?>> Predicate resolve(EHistStatus status, @NonNull CriteriaBuilder cb,
                                                                    @NonNull Path<T> histPath, @NonNull Instant moment) {
        if (isNull(status)) return cb.conjunction();
        return switch (status) {
            case ACTIVE -> activeAt(cb, histPath, cb.literal(moment));
            case FUTURE -> futureAt(cb, histPath, cb.literal(moment));
            case OBSOLETE -> obsoleteAt(cb, histPath, cb.literal(moment));
            case DELETED -> deleted(cb, histPath);
        };
    }

    public static <T extends AHistoricalEntity<?>> Predicate activeNow(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath) {
        return activeAt(cb, histPath, cb.currentTimestamp().as(Instant.class));
    }

    public static <T extends AHistoricalEntity<?>> Predicate activeAt(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath,
                                                                      @NonNull Expression<Instant> moment) {
        return cb.and(
                cb.lessThanOrEqualTo(histPath.get(AHistoricalEntity_.startDate), moment),
                cb.greaterThan(histPath.get(AHistoricalEntity_.endDate), moment),
                cb.isNull(histPath.get(AHistoricalEntity_.delDate))
        );
    }

    public static <T extends AHistoricalEntity<?>> Predicate futureNow(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath) {
        return futureAt(cb, histPath, cb.currentTimestamp().as(Instant.class));
    }

    public static <T extends AHistoricalEntity<?>> Predicate futureAt(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath,
                                                                      @NonNull Expression<Instant> moment) {
        return cb.and(
                cb.greaterThan(histPath.get(AHistoricalEntity_.startDate), moment),
                cb.isNull(histPath.get(AHistoricalEntity_.delDate))
        );
    }

    public static <T extends AHistoricalEntity<?>> Predicate obsoleteNow(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath) {
        return obsoleteAt(cb, histPath, cb.currentTimestamp().as(Instant.class));
    }

    public static <T extends AHistoricalEntity<?>> Predicate obsoleteAt(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath,
                                                                      @NonNull Expression<Instant> moment) {
        return cb.and(
                cb.lessThanOrEqualTo(histPath.get(AHistoricalEntity_.endDate), moment),
                cb.isNull(histPath.get(AHistoricalEntity_.delDate))
        );
    }

    public static <T extends AHistoricalEntity<?>> Predicate deleted(@NonNull CriteriaBuilder cb, @NonNull Path<T> histPath) {
        return cb.isNotNull(histPath.get(AHistoricalEntity_.delDate));
    }
}
