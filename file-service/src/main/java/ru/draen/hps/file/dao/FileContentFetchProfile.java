package ru.draen.hps.file.dao;

import jakarta.persistence.criteria.From;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.dbms.domain.FileContent;
import ru.draen.hps.common.dbms.domain.FileContent_;
import ru.draen.hps.common.dbms.domain.File_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileContentFetchProfile {
    public static void withFile(From<?, FileContent> root) {
        root.fetch(FileContent_.file).fetch(File_.operator);
    }
}
