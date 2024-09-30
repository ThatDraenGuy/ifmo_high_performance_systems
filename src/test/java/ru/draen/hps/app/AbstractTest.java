package ru.draen.hps.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;

@Import(TestConfig.class)
public class AbstractTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected Validator validator;

    @SneakyThrows
    protected <T> T parseJson(Resource json, Class<T> targetClass) {
        return objectMapper.readValue(json.getFile(), targetClass);
    }
}
