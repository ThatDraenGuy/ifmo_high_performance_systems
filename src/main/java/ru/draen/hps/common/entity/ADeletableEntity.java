package ru.draen.hps.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class ADeletableEntity<ID> implements IEntity<ID> {
    @Column(name = "del_user")
    protected String delUser;

    @Column(name = "del_date")
    protected Instant delDate;

    public boolean isDeleted() {
        return delDate != null;
    }
    public EDelStatus getDelStatus() {
        return isDeleted() ? EDelStatus.DELETED : EDelStatus.ACTIVE;
    }
}
