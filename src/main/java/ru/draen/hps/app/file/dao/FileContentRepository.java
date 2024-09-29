package ru.draen.hps.app.file.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import ru.draen.hps.domain.FileContent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface FileContentRepository {
    Optional<FileContent> findById(@NonNull Long id);
    Optional<FileContent> findById(@NonNull Long id, @NonNull Consumer<Root<FileContent>> fetchProfile);
    FileContent save(@NonNull FileContent fileContent);
    void fillFileBlob(@NonNull FileContent fileContent, @NonNull Consumer<OutputStream> blobProvider);
    <T> T processFileBlob(@NonNull FileContent fileContent, @NonNull Function<InputStream, ? extends  T> blobProcessor);
    void fetchFileBlob(@NonNull FileContent fileContent);
}
