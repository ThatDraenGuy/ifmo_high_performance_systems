package ru.draen.hps.config.auth;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class JwtUtilsTest {

    JwtUtils utils;

    @BeforeEach()
    @SneakyThrows
    void setupUtils(@Value("classpath:auth/test-key") Resource key) {
        utils = new JwtUtils(key.getFile().getAbsolutePath());

    }

    @ParameterizedTest
    @CsvSource({
            "subject,       ACCESS",
            "coolUser52,    ACCESS",
            "coolUser52,    REFRESH",
            "aboba69,       REFRESH"
    })
    void generateTokenTest(String subject, JwtUtils.ETokenType tokenType) {
        String token = utils.generateToken(subject, 1000, tokenType);
        switch (tokenType) {
            case ACCESS -> {
                JwtUtils.AccessToken accessToken = utils.extractAccessToken(token);
                assertEquals(new JwtUtils.AccessToken(subject), accessToken);
            }
            case REFRESH -> {
                JwtUtils.RefreshToken accessToken = utils.extractRefreshToken(token);
                assertEquals(new JwtUtils.RefreshToken(subject), accessToken);
            }
        }
    }
}
