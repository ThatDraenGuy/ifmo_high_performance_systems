package ru.draen.hps.common.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.draen.hps.app.client.dao.ClientRepository;
import ru.draen.hps.app.currency.dao.CurrencyRepository;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.domain.Client;
import ru.draen.hps.domain.Client_;
import ru.draen.hps.domain.Currency;
import ru.draen.hps.domain.Currency_;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_agenericrepository",
        "spring.liquibase.default-schema=test_schema_agenericrepository"
})
@DataJpaTest
@Sql(value = {
        "/operator/setup.sql",
        "/tariffrule/setup.sql",
        "/tariff/setup.sql",
        "/client/setup.sql",
        "/currency/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Rollback
@ComponentScan(basePackageClasses = {CurrencyRepository.class, ClientRepository.class})
public class AGenericRepositoryTest {
    @Autowired
    AGenericRepository<Currency, Long> currencyRepository;

    @Autowired
    AGenericRepository<Client, Long> clientRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void existsTest() {
        assertTrue(currencyRepository.exists((root, cq, cb) -> cb.conjunction()));
        assertFalse(currencyRepository.exists((root, cq, cb) -> cb.disjunction()));

        assertTrue(currencyRepository.exists((root, cq, cb) -> cb.equal(root.get(Currency_.id), 1L)));
        assertTrue(currencyRepository.exists((root, cq, cb) -> cb.equal(root.get(Currency_.code), "RUB")));
        assertFalse(currencyRepository.exists((root, cq, cb) -> cb.equal(root.get(Currency_.code), "ABO")));
    }

    @Test
    void countTest() {
        assertEquals(4L, currencyRepository.count((root, cq, cb) -> cb.conjunction()));
        assertEquals(0L, currencyRepository.count((root, cq, cb) -> cb.disjunction()));

        assertEquals(2L, currencyRepository.count((root, cq, cb) -> cb.like(root.get(Currency_.code), "A%")));
    }

    @Test
    @Transactional
    void deleteTest() {
        assertAll(
                () -> assertTrue(currencyRepository.delete(1L)),
                () -> assertNull(entityManager.find(Currency.class, 1L))
        );

        assertFalse(currencyRepository.delete(73L));

        {
            Currency currency = entityManager.find(Currency.class, 2L);
            currencyRepository.delete(currency);
            assertNull(entityManager.find(Currency.class, 2L));
        }
    }

    @Test
    @Transactional
    void saveTest() {
        Currency currency = new Currency();
        currency.setCode("TST");
        currency.setName("Тестовый энтити");

        Currency saved = currencyRepository.save(currency);
        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(currency.getCode(), saved.getCode()),
                () -> assertEquals(currency.getName(), saved.getName()),
                () -> assertNotNull(entityManager.find(Currency.class, saved.getId()))
        );
    }

    @Test
    @Transactional
    void saveStreamTest() {
        Stream<Currency> stream = Stream.of(
                new Currency(null, "Тестовый энтити", "TST"),
                new Currency(null, "Ещё один тестовый", "TS2")
        );

        currencyRepository.save(stream);
        assertAll(
                () -> assertTrue(currencyRepository.exists((root, cq, cb) -> cb.equal(root.get(Currency_.code), "TST"))),
                () -> assertTrue(currencyRepository.exists((root, cq, cb) -> cb.equal(root.get(Currency_.code), "TS2")))
        );
    }

    @Test
    void findAllPageTest() {
        {
            List<Currency> entities = currencyRepository.findAll((root, cq, cb) -> cb.conjunction(),
                    new PageCondition(0, 10, Sort.by(Currency_.ID)));
            List<Currency> expected = List.of(
                    new Currency(1L, "Рубль", "RUB"),
                    new Currency(2L, "Dollar", "USD"),
                    new Currency(3L, "ARP", "ARP"),
                    new Currency(4L, "ATS", "ATS"));
            assertTrue(CollectionUtils.isEqualCollection(entities, expected), () -> entities + "\n" + expected);
        }

        {

            List<Currency> entities = currencyRepository.findAll((root, cq, cb) -> cb.conjunction(),
                    new PageCondition(0, 2, Sort.by(Currency_.ID)));
            List<Currency> expected = List.of(
                    new Currency(1L, "Рубль", "RUB"),
                    new Currency(2L, "Dollar", "USD"));

            assertTrue(CollectionUtils.isEqualCollection(entities, expected), () -> entities + "\n" + expected);
        }

        {

            List<Currency> entities = currencyRepository.findAll((root, cq, cb) -> cb.conjunction(),
                    new PageCondition(1, 2, Sort.by(Currency_.ID)));
            List<Currency> expected = List.of(
                    new Currency(3L, "ARP", "ARP"),
                    new Currency(4L, "ATS", "ATS"));

            assertTrue(CollectionUtils.isEqualCollection(entities, expected), () -> entities + "\n" + expected);
        }
    }

    @Test
    void findAllScrollTest() {
        {
            List<Currency> entities = currencyRepository.findAll((root, cq, cb) -> cb.conjunction(),
                    new ScrollCondition(0, 10, Sort.by(Currency_.ID)));
            List<Currency> expected = List.of(
                    new Currency(1L, "Рубль", "RUB"),
                    new Currency(2L, "Dollar", "USD"),
                    new Currency(3L, "ARP", "ARP"),
                    new Currency(4L, "ATS", "ATS"));
            assertTrue(CollectionUtils.isEqualCollection(entities, expected), () -> entities + "\n" + expected);
        }

        {
            List<Currency> entities = currencyRepository.findAll((root, cq, cb) -> cb.conjunction(),
                    new ScrollCondition(0, 3, Sort.by(Currency_.ID)));
            List<Currency> expected = List.of(
                    new Currency(1L, "Рубль", "RUB"),
                    new Currency(2L, "Dollar", "USD"),
                    new Currency(3L, "ARP", "ARP"));
            assertTrue(CollectionUtils.isEqualCollection(entities, expected), () -> entities + "\n" + expected);
        }

        {
            List<Currency> entities = currencyRepository.findAll((root, cq, cb) -> cb.conjunction(),
                    new ScrollCondition(2, 10, Sort.by(Currency_.ID)));
            List<Currency> expected = List.of(
                    new Currency(3L, "ARP", "ARP"),
                    new Currency(4L, "ATS", "ATS"));
            assertTrue(CollectionUtils.isEqualCollection(entities, expected), () -> entities + "\n" + expected);
        }
    }

    @Test
    void fetchSupportTest() {
        Client noFetch = clientRepository.findOne((root, cq, cb) -> cb.equal(root.get(Client_.id), 1L),
                root -> {}).orElseThrow();
        assertAll(
                () -> assertFalse(Persistence.getPersistenceUtil().isLoaded(noFetch.getOperator())),
                () -> assertFalse(Persistence.getPersistenceUtil().isLoaded(noFetch.getTariff()))
        );

        Client opFetch = clientRepository.findOne((root, cq, cb) -> cb.equal(root.get(Client_.id), 1L),
                root -> root.fetch(Client_.operator)).orElseThrow();
        assertAll(
                () -> assertTrue(Persistence.getPersistenceUtil().isLoaded(opFetch.getOperator())),
                () -> assertFalse(Persistence.getPersistenceUtil().isLoaded(opFetch.getTariff()))
        );
    }
}
