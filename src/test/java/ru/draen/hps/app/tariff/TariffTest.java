package ru.draen.hps.app.tariff;

import jakarta.validation.groups.Default;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.draen.hps.app.AbstractTest;
import ru.draen.hps.app.tariff.controller.TariffController;
import ru.draen.hps.app.tariff.controller.dto.TariffDto;
import ru.draen.hps.common.validation.groups.Create;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_tariff",
        "spring.liquibase.default-schema=test_schema_tariff"
})
@SpringBootTest
@Sql({
        "/operator/setup.sql",
        "/tariffrule/setup.sql"
})
public class TariffTest extends AbstractTest {
    @Autowired
    TariffController tariffController;

    @Test
    void createTest(@Value("classpath:tariff/create.json") Resource json) {
        TariffDto value = parseJson(json, TariffDto.class);
        Assertions.assertTrue(validator.validate(value, Create.class, Default.class).isEmpty());

        ResponseEntity<TariffDto> response = tariffController.create(value);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
