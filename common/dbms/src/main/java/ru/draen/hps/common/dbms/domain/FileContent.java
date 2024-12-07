package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.IEntity;

import java.io.Serializable;

@Entity
@Table(name = "file_content")
@Getter
@Setter
public class FileContent implements IEntity<Long>, Serializable {
    @Id
    @Column(name = "file_file_id")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY, mappedBy = "content")
    @MapsId
    private File file;

    @Column(name = "check_sum", nullable = false)
    private Long checkSum;

    @Column(name = "data_id", nullable = false)
    private Long dataId;

    @Transient
    private byte[] data;
}
