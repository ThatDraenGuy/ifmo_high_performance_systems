package ru.draen.hps.common.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.draen.hps.app.operator.dao.OperatorRepository;
import ru.draen.hps.app.tariff.dao.TariffHistRepository;
import ru.draen.hps.common.entity.EHistStatus;
import ru.draen.hps.domain.Operator;
import ru.draen.hps.domain.TariffHist;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_predicatebuilder",
        "spring.liquibase.default-schema=test_schema_predicatebuilder"
})
@DataJpaTest
@Sql(value = {
        "/operator/setup.sql",
        "/tariffrule/setup.sql",
        "/tariff/setup.sql",
        "/client/setup.sql",
        "/file/setup.sql",
        "/cdrfile/setup.sql",
        "/report/setup.sql",
        "/cdrdata/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ComponentScan(basePackageClasses = {OperatorRepository.class, TariffHistRepository.class})
public class PredicateBuilderTest {

    @PersistenceContext
    EntityManager entityManager;

    @ParameterizedTest
    @MethodSource("addIfNotNullArgs")
    void addIfNotNullTest(AddIfNotNullArgs<Operator> args) {
        List<Operator> list = getTypedQuery(Operator.class, (root, cq, cb) -> new PredicateBuilder(cb)
                    .addIfNotNull(args.testItem, ignored -> args.predicateProvider.toPredicate(root, cq, cb))
                    .toPredicate(PredicateBuilder.EPredicateMode.AND))
                    .getResultList();
        assertTrue(args.checkFunc.apply(list));
    }

    private record AddIfNotNullArgs<T>(Integer testItem, Specification<T> predicateProvider, Function<Collection<T>, Boolean> checkFunc) {}

    private static Stream<AddIfNotNullArgs<Operator>> addIfNotNullArgs() {
        return Stream.of(
                new AddIfNotNullArgs<>(
                        null, (root, cq, cb) -> cb.disjunction(), CollectionUtils::isNotEmpty
                ),
                new AddIfNotNullArgs<>(
                        1, (root, cq, cb) -> cb.disjunction(), CollectionUtils::isEmpty
                ),
                new AddIfNotNullArgs<>(
                        1, (root, cq, cb) -> cb.conjunction(), CollectionUtils::isNotEmpty
                )
        );
    }

    @ParameterizedTest
    @MethodSource("addActualArgs")
    void addActualTest(AddActualArgs args) {
        List<TariffHist> list = getTypedQuery(TariffHist.class, (root, cq, cb) -> new PredicateBuilder(cb)
                .addActual(args.moment, root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND))
                .getResultList();

        assertEquals(args.expectedCount, list.size());
    }

    private record AddActualArgs(OffsetDateTime moment, int expectedCount) {}

    private static Stream<AddActualArgs> addActualArgs() {
        return Stream.of(
                new AddActualArgs(
                        OffsetDateTime.of(2010, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC),
                        2
                ),
                new AddActualArgs(
                        OffsetDateTime.of(2009, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC),
                        1
                ),
                new AddActualArgs(
                        OffsetDateTime.of(2008, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC),
                        0
                )
        );
    }

    @ParameterizedTest
    @MethodSource("addStatusArgs")
    void addStatusTest(AddStatusArgs args) {
        List<TariffHist> list = getTypedQuery(TariffHist.class, (root, cq, cb) -> new PredicateBuilder(cb)
                .addStatus(args.status, root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND))
                .getResultList();
        assertEquals(args.expectedCount, list.size());
    }

    private record AddStatusArgs(EHistStatus status, int expectedCount) {}

    private static Stream<AddStatusArgs> addStatusArgs() {
        return Stream.of(
                new AddStatusArgs(EHistStatus.ACTIVE, 2),
                new AddStatusArgs(EHistStatus.OBSOLETE, 1),
                new AddStatusArgs(EHistStatus.FUTURE, 0),
                new AddStatusArgs(EHistStatus.DELETED, 1)
        );
    }

    private<T> TypedQuery<T> getTypedQuery(Class<T> entityClass, Specification<T> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.where(spec.toPredicate(root, cq, cb));
        return entityManager.createQuery(cq);
    }
}
