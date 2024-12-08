package ru.draen.hps.common.webmvc.utils;

import com.hazelcast.map.IMap;

import java.util.Optional;
import java.util.function.Supplier;

public class CacheUtils {
    public static <E, ID> Optional<E> cacheGet(IMap<ID, E> cache, ID id, Supplier<Optional<E>> getter) {
        E cached = cache.get(id);
        if (cached != null) return Optional.of(cached);
        return getter.get().map(entity -> {
            cache.put(id, entity);
            return entity;
        });
    }

    public static <E, ID> boolean cacheDelete(IMap<ID, E> cache, ID id, Supplier<Boolean> deleter) {
        boolean res = deleter.get();
        if (res) {
            cache.delete(id);
        }
        return res;
    }
}
