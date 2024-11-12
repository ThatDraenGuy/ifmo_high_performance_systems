package ru.draen.hps.account.app.user.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.account.app.user.service.UserService;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.security.config.auth.AppUserDetails;

@RestController
@RequestMapping(value = "${api.prefix}/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final IMapper<User, AppUserDetails> userDetailsMapper;

    @GetMapping
    public ResponseEntity<AppUserDetails> find(@NotNull @RequestParam("username") String username) {
        return ResponseEntity.of(userService.findByUsername(username).map(userDetailsMapper::toDto));
    }
}
