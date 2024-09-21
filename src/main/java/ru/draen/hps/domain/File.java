package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.entity.ADeletableEntity;

@Entity
@Table(name = "files")
@Getter
@Setter
public class File extends ADeletableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_file_id_gen")
    @SequenceGenerator(name = "files_file_id_gen", sequenceName = "files_file_id_seq", allocationSize = 1)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "oper_oper_id")
    private Long operId;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id") // делаем Join с этой стороны из-за проблемы с lazy-фетчингом
    private FileContent content;
}
