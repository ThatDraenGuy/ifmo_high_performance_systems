package ru.draen.hps.notification.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ru.draen.hps.notification.model.NotificationInfo;
import ru.draen.hps.notification.service.NotificationSink;

@Controller
public class NotificationRSocketController {
    @MessageMapping("/subscribe")
    public Flux<NotificationInfo> subscribe() {
        return NotificationSink.SINK.asFlux();
    }
}
