package ru.draen.hps.app.tariffrule;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_tariffrule",
        "spring.liquibase.default-schema=test_schema_tariffrule"
})
@SpringBootTest
@Sql(value = {
        "/operator/setup.sql",
        "/tariffrule/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
public class TariffRuleTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @SneakyThrows
    void findTest() {
        mockMvc.perform(get("/api/v1/tariff-rules").queryParam("operatorId", "3").with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/tariff-rules/paged").queryParam("operatorId", "3").with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createTest(@Value("classpath:tariffrule/create.json") Resource json) {
        mockMvc.perform(post("/api/v1/tariff-rules/").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.getContentAsByteArray())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}
