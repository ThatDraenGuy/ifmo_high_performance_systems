package ru.draen.hps.account.app.operator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.draen.hps.account.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.account.common.dto.LanguageDto;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_operator",
        "spring.liquibase.default-schema=test_schema_operator"
})
@SpringBootTest
@Sql(value = {
        "/operator/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
public class OperatorTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void findByIdTest() {
        mockMvc.perform(get("/api/v1/operators/{id}", 103).with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new OperatorDto(
                        103L,
                        "Мегафон",
                        "RUSNW",
                        List.of(new LanguageDto(1L, "RUS", "Русский"),
                                new LanguageDto(2L, "ENG", "Английский"))
                ))));

        mockMvc.perform(get("/api/v1/operators/{id}", 431).with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
