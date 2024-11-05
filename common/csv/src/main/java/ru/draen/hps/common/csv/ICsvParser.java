package ru.draen.hps.common.csv;

import lombok.NonNull;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ICsvParser<T> {
    List<T> parse(@NonNull InputStream content);
    void parse(@NonNull InputStream content, @NonNull Consumer<T> itemProcessor);
    Stream<T> parseStream(@NonNull InputStream content);
}
