package ru.draen.hps.common.r2dbcdao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Getter
@Setter
public abstract class ADeletableEntity<ID> implements IEntity<ID> {
    @Column("del_user")
    protected String delUser;

    @Column("del_date")
    protected Instant delDate;

    public boolean isDeleted() {
        return delDate != null;
    }
    public EDelStatus getDelStatus() {
        return isDeleted() ? EDelStatus.DELETED : EDelStatus.ACTIVE;
    }
}
