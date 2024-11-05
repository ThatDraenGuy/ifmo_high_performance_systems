package ru.draen.hps.common.csv;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import ru.draen.hps.common.csv.exception.CsvParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class ACsvParserBase<T> implements ICsvParser<T> {

    @Override
    public List<T> parse(@NonNull InputStream content) {
        ImmutablePair<List<T>, List<CsvError>> results = processWholeStream(content);
        if (!results.right.isEmpty()) {
            throw new CsvParseException(results.right);
        }
        return results.left;
    }

    public Stream<T> parseStream(@NonNull InputStream content) {
        List<CsvError> errors = new ArrayList<>();
        CSVReader csvReader = csvReaderBuilder(new InputStreamReader(content)).build();
        CsvToBean<T> csbToBean = csvToBeanBuilder(csvReader)
                .withExceptionHandler(e -> {
                    errors.add(new CsvError(e));
                    return null;
                })
                .build();
        return csbToBean.stream().onClose(() -> {
            if (!errors.isEmpty()) {
                throw new CsvParseException(errors);
            }
        });
    }

    @Override
    public void parse(@NonNull InputStream content, @NonNull Consumer<T> itemProcessor) {
        List<CsvError> errors = processStream(content, itemProcessor);
        if (!errors.isEmpty()) {
            throw new CsvParseException(errors);
        }
    }

    private List<CsvError> processStream(InputStream inputStream, Consumer<T> itemProcessor) {
        List<CsvError> errors = new ArrayList<>();
        CSVReader csvReader = csvReaderBuilder(new InputStreamReader(inputStream)).build();
        CsvToBean<T> csbToBean = csvToBeanBuilder(csvReader)
                .withExceptionHandler(e -> {
                    errors.add(new CsvError(e));
                    return null;
                })
                .build();
        csbToBean.stream().forEach(itemProcessor);
        return errors;
    }

    private ImmutablePair<List<T>, List<CsvError>> processWholeStream(InputStream inputStream) {
        List<CsvError> errors = new ArrayList<>();
        CSVReader csvReader = csvReaderBuilder(new InputStreamReader(inputStream)).build();
        CsvToBean<T> csbToBean = csvToBeanBuilder(csvReader)
                .withExceptionHandler(e -> {
                    errors.add(new CsvError(e));
                    return null;
                })
                .build();

        List<T> values = csbToBean.parse();
        return new ImmutablePair<>(values, errors);
    }

    protected CSVReaderBuilder csvReaderBuilder(Reader reader) {
        return new CSVReaderBuilder(reader)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .withCSVParser(new CSVParserBuilder().build());
    }

    protected CsvToBeanBuilder<T> csvToBeanBuilder(CSVReader csvReader) {
        return new CsvToBeanBuilder<T>(csvReader)
                .withType(resultClass())
                .withIgnoreEmptyLine(true)
                .withIgnoreLeadingWhiteSpace(false)
                .withOrderedResults(true);
    }

    protected abstract Class<T> resultClass();
}
