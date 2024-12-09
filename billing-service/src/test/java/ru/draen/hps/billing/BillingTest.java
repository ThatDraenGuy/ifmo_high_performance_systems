package ru.draen.hps.billing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

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
@EmbeddedKafka(bootstrapServersProperty = "app.kafka.url")
class BillingTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void performTest(@Value("classpath:billing/request.json") Resource json) {
        webTestClient.post().uri("/api/v1/billing/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json.getContentAsByteArray()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @SneakyThrows
    void invalidTariffTest(@Value("classpath:billing/invalidTariffRequest.json") Resource json) {
        webTestClient.post().uri("/api/v1/billing/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json.getContentAsByteArray()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @SneakyThrows
    void missingFileTest(@Value("classpath:billing/missingFileRequest.json") Resource json) {
        webTestClient.post().uri("/api/v1/billing/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json.getContentAsByteArray()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}

