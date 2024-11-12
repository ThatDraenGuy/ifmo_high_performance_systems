package ru.draen.hps.file.dao;

import jakarta.persistence.criteria.From;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.dbms.domain.File_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileFetchProfile {
    public static void brief(From<?, File> root) {
    }

    public static void withContent(From<?, File> root) {
        root.fetch(File_.content);
        root.fetch(File_.operator);
    }
}
