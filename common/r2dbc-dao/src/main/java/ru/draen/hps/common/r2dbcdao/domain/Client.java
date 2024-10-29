package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.ADeletableEntity;

@Table(name = "clients")
@Getter
@Setter
public class Client extends ADeletableEntity<Long> {
    @Id
    @Column("cli_id")
    private Long id;

    @Column("phone_number")
    private String phoneNumber;

    @Column("oper_oper_id")
    private Long operatorId;

    @Column("trff_trff_id")
    private Long tariffId;
}
