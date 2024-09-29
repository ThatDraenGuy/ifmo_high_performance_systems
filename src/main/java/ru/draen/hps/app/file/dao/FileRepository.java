package ru.draen.hps.app.file.dao;

import lombok.NonNull;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.File;

import java.io.OutputStream;
import java.util.function.Consumer;

public interface FileRepository extends ISearchRepository<File, Long> {
    File saveWithContent(@NonNull File file);
    File saveWithContentProvider(@NonNull File file, @NonNull Consumer<OutputStream> contentProvider);
    boolean delete(@NonNull Long id);
}
