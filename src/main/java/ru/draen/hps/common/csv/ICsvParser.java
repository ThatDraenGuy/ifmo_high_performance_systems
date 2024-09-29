package ru.draen.hps.common.csv;

import lombok.NonNull;
import ru.draen.hps.domain.FileContent;

import java.util.List;
import java.util.function.Consumer;

public interface ICsvParser<T> {
    List<T> parse(@NonNull FileContent fileContent);
    void parse(@NonNull FileContent fileContent, @NonNull Consumer<T> itemProcessor);
}
