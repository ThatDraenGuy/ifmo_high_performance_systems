package ru.draen.hps.app.file.dao;

import jakarta.persistence.criteria.From;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.draen.hps.domain.File;
import ru.draen.hps.domain.File_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileFetchProfile {
    public static void brief(From<?, File> root) {
    }

    public static void withContent(From<?, File> root) {
        root.fetch(File_.content);
    }
}
