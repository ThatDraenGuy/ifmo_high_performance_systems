package ru.draen.hps.app.file.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.file.controller.dto.FileCondition;
import ru.draen.hps.common.utils.EMatchMode;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.File;
import ru.draen.hps.domain.File_;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileSpecification {
    public static Specification<File> getByCondition(FileCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.getFileId(), fileId -> cb.equal(root.get(File_.id), fileId))
                .addLike(condition.getFileName(), root.get(File_.fileName), EMatchMode.ANYWHERE, true)
                .addIfNotNull(condition.getOperatorId(), operId -> cb.equal(root.get(File_.operId), operId))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
