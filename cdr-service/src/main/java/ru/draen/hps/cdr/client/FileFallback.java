package ru.draen.hps.cdr.client;

import org.springframework.cloud.openfeign.support.SpringMvcContract;
import reactivefeign.FallbackFactory;
import reactivefeign.ReactiveContract;
import reactivefeign.webclient.WebReactiveFeign;

public class FileFallback implements FallbackFactory<FileClient> {
    @Override
    public FileClient apply(Throwable throwable) {
        return WebReactiveFeign.<FileClient>builder()
                .contract(new ReactiveContract(new SpringMvcContract()))
                .target(FileClient.class, "file-service-fallback:8089", "");
    }
}
