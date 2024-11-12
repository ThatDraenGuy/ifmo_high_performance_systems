package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.common.jpadao.entity.IEntity;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency implements IEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currencies_cur_id_gen")
    @SequenceGenerator(name = "currencies_cur_id_gen", sequenceName = "currencies_cur_id_seq", allocationSize = 1)
    @Column(name = "cur_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;
}
