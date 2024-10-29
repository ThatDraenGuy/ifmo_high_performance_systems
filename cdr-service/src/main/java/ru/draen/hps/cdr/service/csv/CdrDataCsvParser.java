package ru.draen.hps.cdr.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.validators.RowFunctionValidator;
import org.springframework.stereotype.Service;
import ru.draen.hps.cdr.I18n;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.csv.ACsvParserBase;

import java.io.Reader;
import java.util.regex.Pattern;

@Service
public class CdrDataCsvParser extends ACsvParserBase<CdrDataItem> {
    private static final ILabelService lbs = I18n.getLabelService();


    private static final BeanVerifier<CdrDataItem> PHONE_NUMBER_VERIFIER =
            item -> {
        Pattern pattern = Pattern.compile("^[1-9][0-9]{10}$");
        if (!pattern.matcher(item.getPhoneNumber()).matches()) {
            throw new CsvConstraintViolationException(
                    lbs.msg("CdrDataCsvParser.csvValidation.phoneNumber", item.getPhoneNumber()));
        }
        return true;
    };

    private static final RowFunctionValidator COLUMN_COUNT_VALIDATOR = new RowFunctionValidator(row -> row.length == 4,
            lbs.msg("CdrDataCsvParser.csvValidation.columnCount", 4));

    @Override
    protected Class<CdrDataItem> resultClass() {
        return CdrDataItem.class;
    }

    @Override
    protected CSVReaderBuilder csvReaderBuilder(Reader reader) {
        return super.csvReaderBuilder(reader)
                .withRowValidator(COLUMN_COUNT_VALIDATOR);
    }

    @Override
    protected CsvToBeanBuilder<CdrDataItem> csvToBeanBuilder(CSVReader csvReader) {
        return super.csvToBeanBuilder(csvReader)
                .withVerifier(PHONE_NUMBER_VERIFIER);
    }
}
