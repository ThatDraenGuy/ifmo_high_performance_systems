package ru.draen.hps.cdr;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_file",
        "spring.liquibase.default-schema=test_schema_file"
})
@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@EmbeddedKafka(bootstrapServersProperty = "app.kafka.url")
class CdrTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort()
                + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void parseTest(@Value("classpath:cdrfile/request.json") Resource json) {
        webTestClient.post().uri("/api/v1/cdr-files/parse")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json.getContentAsByteArray()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @SneakyThrows
    void findByIdTest() {
        webTestClient.get().uri("/api/v1/cdr-files/{id}", 101)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/api/v1/cdr-files/{id}", 102)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
        webTestClient.get().uri("/api/v1/cdr-files/{id}", 431)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @SneakyThrows
    void findRecords() {
        webTestClient.get().uri("/api/v1/cdr-files/{id}/records?clientId={clientId}", 101, 101)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("""
                        [
                            {"cdr_data_id":105,"cdr_file_id":101,"report_id":null,"client_id":101,"direction":"OUT","start_time":1726547700.000000000,"end_time":1726547835.000000000,"minutes":3,"cost":5.0000000000},
                            {"cdr_data_id":101,"cdr_file_id":101,"report_id":null,"client_id":101,"direction":"INC","start_time":1726547940.000000000,"end_time":1726548255.000000000,"minutes":null,"cost":null},
                            {"cdr_data_id":102,"cdr_file_id":101,"report_id":null,"client_id":101,"direction":"INC","start_time":1726551540.000000000,"end_time":1726562655.000000000,"minutes":null,"cost":null},
                            {"cdr_data_id":103,"cdr_file_id":101,"report_id":null,"client_id":101,"direction":"INC","start_time":1726565940.000000000,"end_time":1726569855.000000000,"minutes":null,"cost":null},
                            {"cdr_data_id":104,"cdr_file_id":101,"report_id":null,"client_id":101,"direction":"INC","start_time":1726573140.000000000,"end_time":1726577055.000000000,"minutes":null,"cost":null}
                        ]
                        """);

        webTestClient.get().uri("/api/v1/cdr-files/{id}/records?clientId={clientId}", 431, 101)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Autowired
    private ConnectionFactory connectionFactory;

    private void executeScriptBlocking(final Resource sqlScript) {
        Mono.from(connectionFactory.create())
                .flatMap(connection -> ScriptUtils.executeSqlScript(connection, sqlScript))
                .block();
    }

    @BeforeEach
    public void rollOutTestData(@Value("classpath:cdrfile/setup.sql") Resource script) {
        executeScriptBlocking(script);
    }

    @AfterEach
    public void cleanUpTestData(@Value("classpath:cdrfile/delete.sql") Resource script) {
        executeScriptBlocking(script);
    }
}
