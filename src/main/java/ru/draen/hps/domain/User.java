package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.entity.DeletableEntity;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends DeletableEntity<Long> {
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = EUserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<EUserRole> roles;
}
