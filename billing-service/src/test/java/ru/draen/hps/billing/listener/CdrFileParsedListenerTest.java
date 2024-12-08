package ru.draen.hps.billing.listener;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.billing.service.BillingService;
import ru.draen.hps.billing.config.AppConfiguration;
import ru.draen.hps.billing.producer.BillingPerformedProducer;
import ru.draen.hps.billing.producer.CdrFileCancelProducer;
import ru.draen.hps.common.core.exception.NotFoundException;
import ru.draen.hps.common.messaging.model.FileRelatedMsg;
import ru.draen.hps.common.webflux.saga.SagaStep;
import ru.draen.hps.common.webflux.utils.SagaUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CdrFileParsedListenerTest {
    private BillingService billingService;
    private BillingPerformedProducer billingPerformedProducer;
    private CdrFileCancelProducer cdrFileCancelProducer;

    private CdrFileParsedListener listener;

    @BeforeEach
    void setup() {
        billingService = mock(BillingService.class);
        when(billingService.perform(any())).thenAnswer(invocation -> {
            BillingRequest req = invocation.getArgument(0);
            return req.cdrFileId() == 1 ? Mono.just(1L) : Mono.error(NotFoundException::new);
        });

        billingPerformedProducer = mock(BillingPerformedProducer.class);
        when(billingPerformedProducer.send(any())).thenReturn(Mono.empty());

        cdrFileCancelProducer = mock(CdrFileCancelProducer.class);
        when(cdrFileCancelProducer.send(any(), any())).thenAnswer(invocation -> {
            Throwable cause = invocation.getArgument(1);
            return Mono.empty();
        });

        SagaStep<BillingRequest, Void> sagaStep = new AppConfiguration()
                .billingSagaStep(billingService, billingPerformedProducer, cdrFileCancelProducer);

        listener = new CdrFileParsedListener(sagaStep);
    }

    @Test
    void fileParsed() {
        listener.listenCdrFileParsed(new FileRelatedMsg(1L))
                .subscribe();
        verify(billingService).perform(new BillingRequest(1L));
        verify(billingPerformedProducer).send(1L);
        verifyNoInteractions(cdrFileCancelProducer);
    }

    @Test
    void fileNotFound() {
        listener.listenCdrFileParsed(new FileRelatedMsg(2L))
                .subscribe();
        verify(billingService).perform(new BillingRequest(2L));
        verifyNoInteractions(billingPerformedProducer);
        verify(cdrFileCancelProducer).send(eq(2L), any());
    }
}
