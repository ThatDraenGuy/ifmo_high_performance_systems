package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.draen.hps.common.entity.IEntity;

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
