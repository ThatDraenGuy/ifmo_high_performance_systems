package ru.draen.hps.app.file.dao;

import lombok.NonNull;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.File;

public interface FileRepository extends ISearchRepository<File, Long> {
    File save(File file);
    boolean delete(@NonNull Long id);
}
