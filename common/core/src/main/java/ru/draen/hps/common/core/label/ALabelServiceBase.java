package ru.draen.hps.common.core.label;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import ru.draen.hps.common.core.exception.NoSuchMessageException;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

@Setter
@Slf4j
public abstract class ALabelServiceBase implements ILabelService {
    private Supplier<Locale> localeResolver;

    @Override
    public String msg(@NonNull String code, Object... args) throws NoSuchMessageException {
        return msg(getDefaultLocale(), code, args);
    }

    @Override
    public String msgDef(@NonNull Locale locale, @NonNull String code, @NonNull String defaultMsg, Object... args) {
        try {
            return msg(locale, code, args);
        } catch (Exception e) {
            return resolveMsg(locale, defaultMsg, args);
        }
    }

    @Override
    public String msgDef(@NonNull String code, @NonNull String defaultMsg, Object... args) {
        return msgDef(getDefaultLocale(), code, defaultMsg, args);
    }

    protected String resolveMsg(Locale locale, String msg, Object... args) {
        if (ArrayUtils.isNotEmpty(args)) {
            MessageFormat format = new MessageFormat(msg, locale);
            return format.format(args);
        } else {
            return msg;
        }
    }

    private Locale getDefaultLocale() {
        Supplier<Locale> localeResolverBind = localeResolver;
        if (!isNull(localeResolverBind)) {
            return localeResolverBind.get();
        } else {
            return Locale.getDefault();
        }
    }
}
