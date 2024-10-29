package ru.draen.hps.file.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.jpadao.utils.EMatchMode;
import ru.draen.hps.common.jpadao.utils.PredicateBuilder;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.dbms.domain.File_;
import ru.draen.hps.common.dbms.domain.Operator_;
import ru.draen.hps.file.controller.dto.FileCondition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileSpecification {
    public static Specification<File> getByCondition(FileCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(condition.getFileId(), fileId -> cb.equal(root.get(File_.id), fileId))
                .addLike(condition.getFileName(), root.get(File_.fileName), EMatchMode.ANYWHERE, true)
                .addIfNotNull(condition.getOperatorId(), operId -> cb.equal(root.get(File_.operator).get(Operator_.id), operId))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
