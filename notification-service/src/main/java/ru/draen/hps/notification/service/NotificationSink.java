package ru.draen.hps.notification.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Sinks;
import ru.draen.hps.notification.model.NotificationInfo;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationSink {
    public static final Sinks.Many<NotificationInfo> SINK = Sinks.many().multicast().onBackpressureBuffer();
}
