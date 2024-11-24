package ru.draen.hps.notification.service;

import reactor.core.publisher.Sinks;
import ru.draen.hps.notification.model.NotificationInfo;

public class NotificationSink {
    public static final Sinks.Many<NotificationInfo> SINK = Sinks.many().multicast().onBackpressureBuffer();
}
