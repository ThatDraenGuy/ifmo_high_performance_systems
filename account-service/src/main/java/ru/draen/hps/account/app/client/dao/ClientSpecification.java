package ru.draen.hps.account.app.client.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.account.app.client.controller.dto.ClientCondition;
import ru.draen.hps.common.dbms.domain.Client;
import ru.draen.hps.common.dbms.domain.Client_;
import ru.draen.hps.common.dbms.domain.Operator_;
import ru.draen.hps.common.jpadao.utils.EMatchMode;
import ru.draen.hps.common.jpadao.utils.PredicateBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSpecification {

    public static Specification<Client> byPhoneNumber(String phoneNumber) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addLike(phoneNumber, root.get(Client_.phoneNumber), EMatchMode.ANYWHERE, true)
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }

    public static Specification<Client> byCondition(ClientCondition condition) {
        return (root, cq, cb) -> new PredicateBuilder(cb)
                .addLike(condition.phoneNumber(), root.get(Client_.phoneNumber), EMatchMode.ANYWHERE, true)
//                .addIfNotNull(condition.operatorId(), operId ->
//                        cb.equal(root.get(Client_.operator).get(Operator_.id), operId))
                .notDeleted(root)
                .toPredicate(PredicateBuilder.EPredicateMode.AND);
    }
}
