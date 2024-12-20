package ru.draen.hps.common.webflux.utils;

import com.hazelcast.map.IMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheUtils {
    public static <E, ID> Mono<E> cacheGet(IMap<ID, E> cache, ID id, Supplier<Mono<E>> getter) {
        return Mono.fromCompletionStage(() -> cache.getAsync(id))
                .switchIfEmpty(getter.get())
                .doOnNext(entity -> cache.putAsync(id, entity));
    }

    public static <E, ID> Mono<Boolean> cacheDelete(IMap<ID, E> cache, ID id, Supplier<Mono<Boolean>> deleter) {
        return deleter.get().doOnNext(res -> {
            if (Boolean.TRUE.equals(res)) {
                cache.deleteAsync(id);
            }
        });
    }
}
