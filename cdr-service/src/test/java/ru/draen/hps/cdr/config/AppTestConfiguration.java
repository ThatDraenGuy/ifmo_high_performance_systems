package ru.draen.hps.cdr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;
import ru.draen.hps.cdr.Main;
import ru.draen.hps.cdr.client.AccountClient;
import ru.draen.hps.cdr.client.FileClient;
import ru.draen.hps.cdr.common.model.ClientModel;
import ru.draen.hps.cdr.common.model.FileContentModel;
import ru.draen.hps.cdr.common.model.FileModel;
import ru.draen.hps.cdr.common.model.OperatorBriefModel;
import ru.draen.hps.common.webflux.config.AuthConfigurationStub;
import ru.draen.hps.common.webflux.config.CommonConfiguration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@EnableWebFlux
@Import({CommonConfiguration.class, AuthConfigurationStub.class, Main.class})
public class AppTestConfiguration {
    @Bean
    @Primary
    FileClient fileClient() {
        FileClient fileClient = mock(FileClient.class);
        when(fileClient.getFile(eq(101L))).thenReturn(Mono.just(new FileModel(
                        101L,
                        "cdr_RUSNW_20240901_20241001.csv",
                        new OperatorBriefModel(
                                103L,
                                "Мегафон",
                                "RUSNW"
                        ),
                        new FileContentModel(
                                101L,
                                "INC,79111234567,20240917073900,20240917074415".getBytes()
                        )
        )));
        when(fileClient.getFile(eq(102L))).thenReturn(Mono.just(new FileModel(
                102L,
                "cdr_RUSNW_20240901_20241001.csv",
                new OperatorBriefModel(
                        103L,
                        "Мегафон",
                        "RUSNW"
                ),
                new FileContentModel(
                        102L,
                        "INC,79111234567,20240917073900,20240917074415".getBytes()
                )
        )));
        when(fileClient.getFile(eq(431L))).thenReturn(Mono.empty());
        return fileClient;
    }

    @Bean
    @Primary
    AccountClient accountClient() {
        AccountClient accountClient = mock(AccountClient.class);
        when(accountClient.findOperatorById(eq(103L))).thenReturn(Mono.just(new OperatorBriefModel(
                103L,
                "Мегафон",
                "RUSNW"
        )));
        when(accountClient.findClientById(eq(101L))).thenReturn(Mono.just(new ClientModel(
                101L,
                "79111234567",
                new OperatorBriefModel(
                        103L,
                        "Мегафон",
                        "RUSNW"
                ),
                101L
        )));
        when(accountClient.findClient(eq("79111234567"))).thenReturn(Mono.just(new ClientModel(
                101L,
                "79111234567",
                new OperatorBriefModel(
                        103L,
                        "Мегафон",
                        "RUSNW"
                ),
                101L
        )));
        return accountClient;
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Bean
    ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }
}
