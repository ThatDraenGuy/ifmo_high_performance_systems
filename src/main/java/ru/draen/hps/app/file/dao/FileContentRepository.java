package ru.draen.hps.app.file.dao;

import lombok.NonNull;
import ru.draen.hps.domain.FileContent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

public interface FileContentRepository {
    FileContent save(@NonNull FileContent fileContent);
    void createFileBlob(@NonNull FileContent fileContent, @NonNull Consumer<OutputStream> blobProvider);
    <T> T processFileBlob(@NonNull FileContent fileContent, @NonNull Function<InputStream, ? extends  T> blobProcessor);
    void fetchFileBlob(@NonNull FileContent fileContent);
}
