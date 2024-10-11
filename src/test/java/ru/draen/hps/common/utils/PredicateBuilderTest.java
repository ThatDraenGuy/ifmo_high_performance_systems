package ru.draen.hps.common.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import java.time.OffsetDateTime;
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

    private record AddIfNotNullArgs<T>(Object testItem, Specification<T> predicateProvider, Function<Collection<T>, Boolean> checkFunc) {}

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
    @MethodSource("addIfNotNullCollArgs")
    void addIfNotNullCollTest(AddIfNotNullCollArgs<Operator> args) {
        List<Operator> list = getTypedQuery(Operator.class, (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(args.testItem, ignored -> args.predicateProvider.toPredicate(root, cq, cb))
                .toPredicate(PredicateBuilder.EPredicateMode.AND))
                .getResultList();
        assertTrue(args.checkFunc.apply(list));
    }

    private record AddIfNotNullCollArgs<T>(Collection<?> testItem, Specification<T> predicateProvider, Function<Collection<T>, Boolean> checkFunc) {}

    private static Stream<AddIfNotNullCollArgs<Operator>> addIfNotNullCollArgs() {
        return Stream.of(
                new AddIfNotNullCollArgs<>(
                        List.of(), (root, cq, cb) -> cb.disjunction(), CollectionUtils::isNotEmpty
                ),
                new AddIfNotNullCollArgs<>(
                        List.of(1), (root, cq, cb) -> cb.disjunction(), CollectionUtils::isEmpty
                ),
                new AddIfNotNullCollArgs<>(
                        List.of(1), (root, cq, cb) -> cb.conjunction(), CollectionUtils::isNotEmpty
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "2010-01-01T12:00:00+00:00,  2",
            "2009-01-01T12:00:00+00:00,  1",
            "2008-01-01T12:00:00+00:00,  0",
            ",                           4"
    })
    void addActualTest(OffsetDateTime moment, int expectedCount) {
        List<TariffHist> list = getTypedQuery(TariffHist.class, (root, cq, cb) -> new PredicateBuilder(cb)
                .addActual(moment, root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND))
                .getResultList();

        assertEquals(expectedCount, list.size());
    }

    @ParameterizedTest
    @CsvSource({
            "ACTIVE,    2",
            "OBSOLETE,  1",
            "FUTURE,    0",
            "DELETED,   1",
            ",      4"
    })
    void addStatusTest(EHistStatus status, int expectedCount) {
        List<TariffHist> list = getTypedQuery(TariffHist.class, (root, cq, cb) -> new PredicateBuilder(cb)
                .addStatus(status, root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND))
                .getResultList();
        assertEquals(expectedCount, list.size());
    }

    @ParameterizedTest
    @CsvSource({
            "ACTIVE,    2010-01-01T12:00:00+00:00,   2",
            "ACTIVE,    2009-01-01T12:00:00+00:00,   1",
            "ACTIVE,    2008-01-01T12:00:00+00:00,   0"
    })
    void addStatusAtTest(EHistStatus status, OffsetDateTime moment, int expectedCount) {
        List<TariffHist> list = getTypedQuery(TariffHist.class, (root, cq, cb) -> new PredicateBuilder(cb)
                .addStatus(status, moment.toInstant(), root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND))
                .getResultList();
        assertEquals(expectedCount, list.size());
    }

    private<T> TypedQuery<T> getTypedQuery(Class<T> entityClass, Specification<T> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.where(spec.toPredicate(root, cq, cb));
        return entityManager.createQuery(cq);
    }
}
