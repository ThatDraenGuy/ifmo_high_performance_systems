package ru.draen.hps.common.core.label;

import lombok.NonNull;
import ru.draen.hps.common.core.exception.NoSuchMessageException;

import java.util.Locale;

public interface ILabelService {
    String msg(@NonNull String code, Object...args) throws NoSuchMessageException;
    String msg(@NonNull Locale locale, @NonNull String code, Object... args) throws NoSuchMessageException;
    String msgDef(@NonNull String code, @NonNull String defaultMsg, Object... args);
    String msgDef(@NonNull Locale locale, @NonNull String code, @NonNull String defaultMsg, Object... args);
}
