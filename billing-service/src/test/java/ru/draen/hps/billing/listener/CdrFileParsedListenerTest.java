package ru.draen.hps.billing.listener;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.billing.controller.dto.BillingRequest;
import ru.draen.hps.billing.app.billing.service.BillingService;
import ru.draen.hps.billing.listener.dto.CdrFileParsedMsg;
import ru.draen.hps.billing.producer.BillingPerformedProducer;
import ru.draen.hps.common.core.exception.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CdrFileParsedListenerTest {
    private BillingService billingService;
    private BillingPerformedProducer billingPerformedProducer;

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

        listener = new CdrFileParsedListener(billingService, billingPerformedProducer);
    }

    @Test
    void fileParsed() {
        listener.listenCdrFileParsed(new CdrFileParsedMsg(1L))
                .subscribe();
        verify(billingService).perform(new BillingRequest(1L));
        verify(billingPerformedProducer).send(1L);
    }

    @Test
    void fileNotFound() {
        listener.listenCdrFileParsed(new CdrFileParsedMsg(2L))
                .subscribe();
        verify(billingService).perform(new BillingRequest(2L));
        verifyNoInteractions(billingPerformedProducer);
    }
}
