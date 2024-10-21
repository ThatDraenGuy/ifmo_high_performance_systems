package ru.draen.hps.app.user.controller.dto;

import lombok.*;
import ru.draen.hps.domain.EUserRole;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private Set<EUserRole> roles;
}
