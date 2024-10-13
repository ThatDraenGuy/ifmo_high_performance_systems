package ru.draen.hps.app;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_paging",
        "spring.liquibase.default-schema=test_schema_paging"
})
@SpringBootTest
@Sql(value = {
        "/operator/setup.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
public class PagingTest {
    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @SneakyThrows
    @CsvSource({
            "0,     10",
            "0,     1",
            "1,     1",
            "10,    10"
    })
    void validPaginationTest(int page, int size) {
        String byParamsValue = mockMvc.perform(get("/api/v1/operators/paged")
                .queryParam("page", String.valueOf(page))
                .queryParam("size", String.valueOf(size))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andReturn().getResponse().getContentAsString();

        String byHeadersValue = mockMvc.perform(get("/api/v1/operators/paged")
                        .header("X-Page", String.valueOf(page))
                        .header("X-Per-Page", String.valueOf(size))
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andReturn().getResponse().getContentAsString();

        assertEquals(byParamsValue, byHeadersValue);
    }

    @ParameterizedTest
    @SneakyThrows
    @CsvSource({
            "0,     0",
            "-1,     1",
            "1,     -1",
            "10,    51"
    })
    void invalidPagingTest(int page, int size) {
        mockMvc.perform(get("/api/v1/operators/paged")
                        .queryParam("page", String.valueOf(page))
                        .queryParam("size", String.valueOf(size))
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @SneakyThrows
    @CsvSource({
            "0,     10",
            "0,     1",
            "1,     1",
            "10,    10"
    })
    void validScrollingTest(int offset, int limit) {
        mockMvc.perform(get("/api/v1/operators")
                        .queryParam("offset", String.valueOf(offset))
                        .queryParam("limit", String.valueOf(limit))
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @SneakyThrows
    @CsvSource({
            "0,      0",
            "-1,     1",
            "1,     -1",
            "10,    51"
    })
    void invalidScrollingTest(int offset, int limit) {
        mockMvc.perform(get("/api/v1/operators")
                        .queryParam("offset", String.valueOf(offset))
                        .queryParam("limit", String.valueOf(limit))
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
