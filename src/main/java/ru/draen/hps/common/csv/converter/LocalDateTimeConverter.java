package ru.draen.hps.common.csv.converter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;
import ru.draen.hps.I18n;
import ru.draen.hps.common.label.ILabelService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;


public class LocalDateTimeConverter<T, I> extends AbstractBeanField<T, I> {
    private static final ILabelService lbs = I18n.getLabelService();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMddHHmmss")
            .withResolverStyle(ResolverStyle.STRICT);
    @Override
    protected Object convert(String value) throws CsvConstraintViolationException {
        try {
            return StringUtils.isBlank(value) ? null : LocalDateTime.parse(value, formatter);
        } catch (DateTimeParseException e) {
            throw new CsvConstraintViolationException(lbs.msg("LocalDateTimeConverter.parseError", value));
        }
    }

    @Override
    protected String convertToWrite(Object value) throws CsvDataTypeMismatchException {
        return switch (value) {
            case LocalDateTime localDateTime -> localDateTime.format(formatter);
            case null -> "";
            default -> throw new CsvDataTypeMismatchException("Not a LocalDateTime");
        };
    }
}
