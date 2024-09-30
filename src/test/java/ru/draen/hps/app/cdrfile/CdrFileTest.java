package ru.draen.hps.app.cdrfile;

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
import ru.draen.hps.app.cdrfile.controller.CdrFileController;
import ru.draen.hps.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.app.cdrfile.controller.dto.ParseCdrRequest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_cdrfile",
        "spring.liquibase.default-schema=test_schema_cdrfile"
})
@SpringBootTest
@Sql({
        "/operator/setup.sql",
        "/tariffrule/setup.sql",
        "/tariff/setup.sql",
        "/client/setup.sql",
        "/file/setup.sql"
})
public class CdrFileTest extends AbstractTest {
    @Autowired
    CdrFileController cdrFileController;

    @Test
    void parseTest(@Value("classpath:cdrfile/request.json") Resource json) {
        ParseCdrRequest request = parseJson(json, ParseCdrRequest.class);
        Assertions.assertTrue(validator.validate(request, Default.class).isEmpty());

        ResponseEntity<CdrFileDto> response = cdrFileController.parse(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
