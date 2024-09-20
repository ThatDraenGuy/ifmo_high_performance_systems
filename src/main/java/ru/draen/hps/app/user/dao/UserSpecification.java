package ru.draen.hps.app.user.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.utils.EMatchMode;
import ru.draen.hps.common.utils.PredicateBuilder;
import ru.draen.hps.domain.User;
import ru.draen.hps.domain.User_;
import ru.draen.hps.app.user.controller.dto.UserCondition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {
    public static Specification<User> getByCondition(UserCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addLike(condition.username(), root.get(User_.username), EMatchMode.ANYWHERE, true)
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }

    public static Specification<User> getByUsername(String username) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .add(() -> cb.equal(root.get(User_.username), username))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
