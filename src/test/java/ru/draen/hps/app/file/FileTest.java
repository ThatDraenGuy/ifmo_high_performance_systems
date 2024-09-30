package ru.draen.hps.app.file;

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
import ru.draen.hps.app.file.controller.FileController;
import ru.draen.hps.app.file.controller.dto.FileBriefDto;
import ru.draen.hps.app.file.controller.dto.FileDto;
import ru.draen.hps.common.validation.groups.Create;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_schema=test_schema_file",
        "spring.liquibase.default-schema=test_schema_file"
})
@SpringBootTest
@Sql({
        "/operator/setup.sql"
})
public class FileTest extends AbstractTest {
    @Autowired
    FileController fileController;

    @Test
    void uploadTest(@Value("classpath:file/upload.json") Resource json) {
        FileDto dto = parseJson(json, FileDto.class);
        Assertions.assertTrue(validator.validate(dto, Create.class, Default.class).isEmpty());

        ResponseEntity<FileBriefDto> response = fileController.upload(dto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
