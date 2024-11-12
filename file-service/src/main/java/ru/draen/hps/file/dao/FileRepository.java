package ru.draen.hps.file.dao;

import lombok.NonNull;
import ru.draen.hps.common.jpadao.dao.ISearchRepository;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.jpadao.dao.IStreamRepository;

import java.io.OutputStream;
import java.util.function.Consumer;

public interface FileRepository extends ISearchRepository<File, Long>, IStreamRepository<File, Long> {
    File saveWithContent(@NonNull File file);
    File saveWithContentProvider(@NonNull File file, @NonNull Consumer<OutputStream> contentProvider);
    boolean delete(@NonNull Long id);
}
