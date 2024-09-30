package ru.draen.hps.app.billing;

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
import ru.draen.hps.app.billing.controller.BillingController;
import ru.draen.hps.app.billing.controller.dto.BillingRequest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_billing",
        "spring.liquibase.default-schema=test_schema_billing"
})
@SpringBootTest
@Sql({
        "/operator/setup.sql",
        "/tariffrule/setup.sql",
        "/tariff/setup.sql",
        "/client/setup.sql",
        "/file/setup.sql",
        "/cdrfile/setup.sql",
        "/cdrdata/setup.sql"
})
public class BillingTest extends AbstractTest {
    @Autowired
    BillingController billingController;

    @Test
    void performTest(@Value("classpath:billing/request.json") Resource json) {
        BillingRequest request = parseJson(json, BillingRequest.class);
        Assertions.assertTrue(validator.validate(request, Default.class).isEmpty());

        ResponseEntity<?> response = billingController.perform(request);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
