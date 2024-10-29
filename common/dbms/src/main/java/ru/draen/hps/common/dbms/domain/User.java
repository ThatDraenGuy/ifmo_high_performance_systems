package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends ADeletableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_gen")
    @SequenceGenerator(name = "users_user_id_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(targetClass = EUserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Enumerated(EnumType.STRING)
    private Set<EUserRole> roles;
}
