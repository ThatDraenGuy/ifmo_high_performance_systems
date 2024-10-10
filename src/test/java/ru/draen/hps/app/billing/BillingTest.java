package ru.draen.hps.app.billing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_billing",
        "spring.liquibase.default-schema=test_schema_billing"
})
@SpringBootTest
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
@AutoConfigureMockMvc
public class BillingTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    void performTest(@Value("classpath:billing/request.json") Resource json) {
        mockMvc.perform(post("/billing/").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getContentAsByteArray())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void performMissingFileTest(@Value("classpath:billing/missingFileRequest.json") Resource json) {
        mockMvc.perform(post("/billing/").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.getContentAsByteArray())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
