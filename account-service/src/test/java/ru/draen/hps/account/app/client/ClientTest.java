package ru.draen.hps.account.app.client;

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
import ru.draen.hps.account.app.client.controller.dto.ClientDto;
import ru.draen.hps.account.app.operator.controller.dto.OperatorBriefDto;
import ru.draen.hps.account.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.account.common.dto.LanguageDto;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_client",
        "spring.liquibase.default-schema=test_schema_client"
})
@SpringBootTest
@Sql(value = {
        "/operator/setup.sql",
        "/tariffrule/setup.sql",
        "/tariff/setup.sql",
        "/client/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
class ClientTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void findByIdTest() {
        mockMvc.perform(get("/api/v1/clients/{id}", 101).with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ClientDto(
                        101L,
                        "79111234567",
                        new OperatorBriefDto(
                                103L,
                                "Мегафон",
                                "RUSNW"
                        ),
                        101L
                ))));

        mockMvc.perform(get("/api/v1/clients/{id}", 431).with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void findSingle() {
        mockMvc.perform(get("/api/v1/clients/single").param("phoneNumber", "79111234567").with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ClientDto(
                        101L,
                        "79111234567",
                        new OperatorBriefDto(
                                103L,
                                "Мегафон",
                                "RUSNW"
                        ),
                        101L
                ))));

        mockMvc.perform(get("/api/v1/clients/single").param("phoneNumber", "19111234567").with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/clients/single").with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
