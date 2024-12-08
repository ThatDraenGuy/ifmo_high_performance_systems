package ru.draen.hps.common.webflux.saga;

import reactor.core.publisher.Mono;

public interface SagaStep<T, R> {
    Mono<R> process(T request);
    Mono<Void> cancel(T request);
}
