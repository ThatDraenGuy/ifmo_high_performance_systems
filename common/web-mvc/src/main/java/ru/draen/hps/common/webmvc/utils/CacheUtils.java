package ru.draen.hps.common.webmvc.utils;

import com.hazelcast.map.IMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheUtils {
    public static <E, ID> Optional<E> cacheGet(IMap<ID, E> cache, ID id, Supplier<Optional<E>> getter) {
        E cached = cache.get(id);
        if (cached != null) return Optional.of(cached);
        return getter.get().map(entity -> {
            cache.put(id, entity);
            return entity;
        });
    }

    public static <E, ID> boolean cacheDelete(IMap<ID, E> cache, ID id, BooleanSupplier deleter) {
        boolean res = deleter.getAsBoolean();
        if (res) {
            cache.delete(id);
        }
        return res;
    }
}
