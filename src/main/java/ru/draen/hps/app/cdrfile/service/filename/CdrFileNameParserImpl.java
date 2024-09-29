package ru.draen.hps.app.cdrfile.service.filename;

import org.springframework.stereotype.Service;
import ru.draen.hps.I18n;
import ru.draen.hps.app.cdrfile.service.CdrParseException;
import ru.draen.hps.common.label.ILabelService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CdrFileNameParserImpl implements CdrFileNameParser {
    private static final ILabelService lbs = I18n.getLabelService();

    private static final String OPER_CODE = "opercode";
    private static final String PERIOD_START = "periodstart";
    private static final String PERIOD_END = "periodend";
    private static final Pattern PATTERN = Pattern.compile(
                "^cdr_(?<" + OPER_CODE + ">\\w{5})_(?<" + PERIOD_START + ">\\d{8})_(?<" + PERIOD_END + ">\\d{8})\\.csv$");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public CdrFileName parse(String fileName) {
        Matcher matcher = PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            throw new CdrParseException(lbs.msg("CdrParseException.wrongFormat", fileName));
        }

        String operCode = matcher.group(OPER_CODE);
        String periodStart = matcher.group(PERIOD_START);
        String periodEnd = matcher.group(PERIOD_END);

        try {
            return new CdrFileName(
                    fileName,
                    operCode,
                    formatter.parse(periodStart, LocalDate::from),
                    formatter.parse(periodEnd, LocalDate::from)
            );
        } catch (DateTimeParseException e) {
            throw new CdrParseException(lbs.msg("CdrParseException.invalidDate"), e);
        }
    }
}
