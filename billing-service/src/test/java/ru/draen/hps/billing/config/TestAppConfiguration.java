package ru.draen.hps.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.client.CdrFileClient;
import ru.draen.hps.billing.common.model.*;
import ru.draen.hps.common.dbms.domain.ECallDirection;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestAppConfiguration {
    @Bean
    @Primary
    CdrFileClient cdrFileClient() {
        CdrFileClient cdrFileClient = mock(CdrFileClient.class);
        when(cdrFileClient.findById(eq(101L))).thenReturn(Mono.just(new CdrFileModel(
                101L,
                new FileBriefModel(
                        101L,
                        "cdr_RUSNW_20240901_20241001.csv",
                        103L
                ),
                OffsetDateTime.of(2024, 8, 31, 21, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2024, 9, 30, 21, 0, 0, 0, ZoneOffset.UTC)
        )));
        when(cdrFileClient.findClients(eq(101L))).thenReturn(Flux.just(
                new ClientModel(
                        101L,
                        "79111234567",
                        new OperatorBriefModel(
                                103L,
                                "Мегафон",
                                "RUSNW"
                        ),
                        101L
                )
        ));
        when(cdrFileClient.findClientRecords(eq(101L), eq(101L))).thenReturn(Flux.just(
                new CdrDataModel(
                        101L,
                        101L,
                        101L,
                        null,
                        ECallDirection.INC,
                        OffsetDateTime.parse("2024-09-17T04:39:00Z"),
                        OffsetDateTime.parse("2024-09-17T04:44:15Z"),
                        null,
                        null
                ),
                new CdrDataModel(
                        102L,
                        101L,
                        101L,
                        null,
                        ECallDirection.INC,
                        OffsetDateTime.parse("2024-09-17T05:39:00Z"),
                        OffsetDateTime.parse("2024-09-17T08:44:15Z"),
                        null,
                        null
                ),
                new CdrDataModel(
                        103L,
                        101L,
                        101L,
                        null,
                        ECallDirection.INC,
                        OffsetDateTime.parse("2024-09-17T09:39:00Z"),
                        OffsetDateTime.parse("2024-09-17T10:44:15Z"),
                        null,
                        null
                ),
                new CdrDataModel(
                        104L,
                        101L,
                        101L,
                        null,
                        ECallDirection.INC,
                        OffsetDateTime.parse("2024-09-17T11:39:00Z"),
                        OffsetDateTime.parse("2024-09-17T12:44:15Z"),
                        null,
                        null
                )
        ));
        when(cdrFileClient.updateRecord(any(), any())).thenAnswer(invocation -> {
            CdrDataModel model = invocation.getArgument(1);
            return Mono.just(model);
        });

        when(cdrFileClient.findById(eq(110L))).thenReturn(Mono.just(new CdrFileModel(
                110L,
                new FileBriefModel(
                        110L,
                        "cdr_RUSNW_20240901_20241001.csv",
                        103L
                ),
                OffsetDateTime.of(2024, 8, 31, 21, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2024, 9, 30, 21, 0, 0, 0, ZoneOffset.UTC)
        )));
        when(cdrFileClient.findClients(eq(110L))).thenReturn(Flux.just(
                new ClientModel(
                        103L,
                        "79211234567",
                        new OperatorBriefModel(
                                103L,
                                "Мегафон",
                                "RUSNW"
                        ),
                        104L
                )
        ));
        when(cdrFileClient.findClientRecords(eq(110L), eq(103L))).thenReturn(Flux.just(
                new CdrDataModel(
                        110L,
                        110L,
                        103L,
                        null,
                        ECallDirection.INC,
                        OffsetDateTime.parse("2024-09-17T04:39:00Z"),
                        OffsetDateTime.parse("2024-09-18T04:44:15Z"),
                        null,
                        null
                )
        ));

        when(cdrFileClient.findById(eq(302L))).thenReturn(Mono.empty());
        return cdrFileClient;
    }
}
