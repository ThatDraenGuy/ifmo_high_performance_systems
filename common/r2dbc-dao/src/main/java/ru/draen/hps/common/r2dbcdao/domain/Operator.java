package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.ADeletableEntity;

@Table("operators")
@Getter
@Setter
public class Operator extends ADeletableEntity<Long> {
    @Id
    @Column("oper_id")
    private Long id;

    @Column("name")
    private String name;

    @Column("code")
    private String code;
}
