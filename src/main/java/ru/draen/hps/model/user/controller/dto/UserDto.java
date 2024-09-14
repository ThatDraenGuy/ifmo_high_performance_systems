package ru.draen.hps.model.user.controller.dto;

import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.domain.EUserRole;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String username;
    private Set<EUserRole> roles;
}
