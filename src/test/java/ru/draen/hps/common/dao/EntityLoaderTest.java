package ru.draen.hps.common.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.draen.hps.common.exception.ProcessingException;
import ru.draen.hps.domain.Currency;
import ru.draen.hps.domain.Operator;


import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_entityloader",
        "spring.liquibase.default-schema=test_schema_entityloader"
})
@DataJpaTest
@Sql(value = {
        "/operator/setup.sql",
        "/currency/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ComponentScan(basePackageClasses = EntityLoader.class)
public class EntityLoaderTest {
    @Autowired
    EntityLoader entityLoader;

    @Test
    void loadNull() {
        Operator operator = null;
        assertNull(entityLoader.load(operator));

        Currency currency = null;
        assertNull(entityLoader.load(currency));
    }


    @Test
    void loadExisting() {
        Operator operator = new Operator();
        operator.setId(101L);
        Operator dbOperator = entityLoader.load(operator);
        assertAll(
                () -> assertEquals(operator.getId(), dbOperator.getId()),
                () -> assertNotNull(dbOperator.getCode()),
                () -> assertNotNull(dbOperator.getName())
        );

        Currency currency = new Currency();
        currency.setId(101L);
        Currency dbCurrency = entityLoader.load(currency);
        assertAll(
                () -> assertEquals(currency.getId(), dbCurrency.getId()),
                () -> assertNotNull(dbCurrency.getCode()),
                () -> assertNotNull(dbCurrency.getName())
        );
    }

    @Test
    void loadNotExisting() {
        Operator operator = new Operator();
        operator.setId(1984L);
        assertThrows(ProcessingException.class, () -> entityLoader.load(operator));

        Currency currency = new Currency();
        currency.setId(42069L);
        assertThrows(ProcessingException.class, () -> entityLoader.load(currency));
    }
}
