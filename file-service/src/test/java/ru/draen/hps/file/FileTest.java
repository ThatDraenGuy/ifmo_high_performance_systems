package ru.draen.hps.file;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.draen.hps.file.controller.dto.FileContentDto;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.controller.dto.OperatorBriefDto;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_file",
        "spring.liquibase.default-schema=test_schema_file"
})
@SpringBootTest
@Sql(value = {
        "/operator/setup.sql",
        "/file/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
public class FileTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void uploadTest(@Value("classpath:file/upload.json") Resource json) {
        webTestClient.post().uri("/api/v1/files/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json.getContentAsByteArray()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @SneakyThrows
    void getTest() {
        webTestClient.get().uri("/api/v1/files/{id}", 101)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(new FileDto(
                        101L,
                        "cdr_RUSNW_20240901_20241001.csv",
                        new OperatorBriefDto(
                                103L,
                                "Мегафон",
                                "RUSNW"
                        ),
                        new FileContentDto(
                                101L,
                                "INC,79111234567,20240917073900,20240917074415".getBytes()
                        )
                )));

        webTestClient.get().uri("/api/v1/files/{id}", 431)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @SneakyThrows
    void deleteTest() {
        webTestClient.delete().uri("/api/v1/files/{id}", 101)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.delete().uri("/api/v1/files/{id}", 431)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
