package ru.draen.hps.common.webflux.utils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import ru.draen.hps.common.webflux.saga.SagaRollbackException;
import ru.draen.hps.common.webflux.saga.SagaStep;

import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public class SagaUtils {
    public static<T, F, R> SagaStep<T, R> createStep(
            Function<T, Long> stepIdProvider,
            Function<T, Mono<F>> processor,
            Function<T, Mono<Void>> rollback,
            Function<F, Mono<R>> next,
            BiFunction<T, Throwable, Mono<F>> onError
    ) {
        return new SagaStep<>() {
            @Override
            public Mono<R> process(T request) {
                long stepId = stepIdProvider.apply(request);
                log.info("SAGA STEP {} STARTED", stepId);
                return processor.apply(request)
                        .doOnSuccess(item -> log.info("SAGA STEP {} PROCESSED", stepId))
                        .doOnError(e -> log.error("SAGA STEP {} ENCOUNTERED ERROR {}", stepId, e.getMessage()))
                        .onErrorResume(e -> onError.apply(request, e)
                                .doOnSuccess(item -> log.info("SAGA STEP {} ERROR HANDLED", stepId))
                                .then(Mono.error(e)))
                        .flatMap(next)
                        .doOnSuccess(item -> log.info("SAGA STEP {} COMPLETED", stepId));
            }

            @Override
            public Mono<Void> cancel(T request) {
                long stepId = stepIdProvider.apply(request);
                return rollback.apply(request)
                        .doOnSuccess(item -> log.info("SAGA STEP {} ROLLBACK APPLIED", stepId))
                        .then(onError
                                .apply(request, new SagaRollbackException())
                                .doOnSuccess(item -> log.info("SAGA STEP {} ROLLBACK COMPLETED", stepId))
                                .then());
            }
        };
    }

    public static<T, F, R> SagaStep<T, R> createStartStep(
            Function<T, Long> stepIdProvider,
            Function<T, Mono<F>> processor,
            Function<F, Long> stepIdProvider2,
            Function<T, Mono<Void>> rollback,
            Function<F, Mono<R>> next,
            BiFunction<T, Throwable, Mono<F>> onError
    ) {
        return new SagaStep<>() {
            @Override
            public Mono<R> process(T request) {
                int hash = request.hashCode();
                log.info("NEW SAGA {} STARTED", request.hashCode());
                return processor.apply(request)
                        .map(item -> Tuples.of(item, stepIdProvider2.apply(item)))
                        .doOnSuccess(tuple -> log.info("NEW SAGA {} PROCESSED - STEP ID CREATED {}", hash, tuple.getT2()))
                        .doOnError(e -> log.error("NEW SAGA {} ENCOUNTERED ERROR {}", hash, e.getMessage()))
                        .onErrorResume(e -> onError.apply(request, e)
                                .doOnSuccess(item -> log.info("NEW SAGA {} ERROR HANDLED", hash))
                                .then(Mono.error(e)))
                        .flatMap(tuple -> next.apply(tuple.getT1()).map(res -> Tuples.of(res, tuple.getT2())))
                        .doOnSuccess(tuple -> log.info("SAGA STEP {} COMPLETED", tuple.getT2()))
                        .map(Tuple2::getT1);
            }

            @Override
            public Mono<Void> cancel(T request) {
                long stepId = stepIdProvider.apply(request);
                return rollback.apply(request)
                        .doOnSuccess(item -> log.info("SAGA STEP {} ROLLBACK APPLIED", stepId))
                        .then(onError
                                .apply(request, new SagaRollbackException())
                                .doOnSuccess(item -> log.info("SAGA STEP {} ROLLBACK COMPLETED", stepId))
                                .then());
            }
        };
    }

    public static<T, R> Mono<R> noOnError(T item, Throwable cause) {
        return Mono.empty();
    }

    public static<T> Mono<Void> noRollback(T item) {
        return Mono.empty();
    }
}
