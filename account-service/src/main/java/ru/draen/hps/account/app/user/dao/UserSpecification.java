package ru.draen.hps.account.app.user.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.dbms.domain.User_;
import ru.draen.hps.common.jpadao.utils.PredicateBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {
    public static Specification<User> byUsername(String username) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addIfNotNull(username, name -> cb.equal(root.get(User_.username), name))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
