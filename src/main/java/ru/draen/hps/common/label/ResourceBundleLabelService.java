package ru.draen.hps.common.label;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import ru.draen.hps.common.exception.NoSuchMessageException;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceBundleLabelService extends AbstractLabelService {
    private final String baseName;
    private final Map<Locale, ResourceBundle> bundles = new ConcurrentHashMap<>();

    @Override
    public String msg(@NonNull Locale locale, @NonNull String code, Object... args) throws NoSuchMessageException {
        ResourceBundle bundle = bundles.computeIfAbsent(locale, l -> ResourceBundle.getBundle(
                baseName, l, ResourceBundle.Control
                        .getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)
        ));
        try {
            return resolveMsg(locale, bundle.getString(code), args);
        } catch (Exception e) {
            throw new NoSuchMessageException(e);
        }
    }

    public static ResourceBundleLabelService build(Function<Config.ConfigBuilder, Config.ConfigBuilder> build) {
        Config config = build.apply(Config.builder()).build();
        String prefix = Optional.ofNullable(config.dispositionMarker)
                .map(marker -> marker.getPackageName() + ".")
                .orElse("");

        ResourceBundleLabelService service = new ResourceBundleLabelService(prefix + config.bundleName);
        service.setLocaleResolver(config.localeResolver);
        return service;
    }

    @Builder
    public static class Config {
        @Builder.Default
        private final String bundleName = "messages";
        private final Class<?> dispositionMarker;
        private final Supplier<Locale> localeResolver;
    }
}
