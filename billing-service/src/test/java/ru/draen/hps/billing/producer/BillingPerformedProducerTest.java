package ru.draen.hps.billing.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import ru.draen.hps.billing.producer.dto.BillingPerformedMsg;
import ru.draen.hps.common.messaging.model.AppMessage;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BillingPerformedProducerTest {
    private KafkaTemplate<String, AppMessage> kafkaTemplate;
    private BillingPerformedProducer billingPerformedProducer;

    @BeforeEach
    void setup() {
        kafkaTemplate = (KafkaTemplate<String, AppMessage>) mock(KafkaTemplate.class);
        when(kafkaTemplate.send(anyString(), any())).thenAnswer(invocation -> {
            String topic = invocation.getArgument(0);
            AppMessage msg = invocation.getArgument(1);
            return CompletableFuture.supplyAsync(() ->
                    new SendResult<>(new ProducerRecord<>(topic, msg), null));
        });

        billingPerformedProducer = new BillingPerformedProducer(kafkaTemplate);
        billingPerformedProducer.setBillingPerformedTopic("topic");
    }

    @Test
    void send() {
        billingPerformedProducer.send(1L).subscribe();

        verify(kafkaTemplate).send("topic", new BillingPerformedMsg(1L));
    }
}
