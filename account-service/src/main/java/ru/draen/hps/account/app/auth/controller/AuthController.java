package ru.draen.hps.account.app.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.account.app.auth.controller.dto.LoginRequest;
import ru.draen.hps.account.app.auth.controller.dto.LoginResponse;
import ru.draen.hps.account.app.auth.controller.dto.RegisterRequest;
import ru.draen.hps.account.app.auth.controller.dto.RegisterResponse;
import ru.draen.hps.account.app.auth.service.AuthService;
import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.security.config.auth.JwtUtils;

@Profile(AppProfile.DEV)
@RestController
@RequestMapping(value = "${api.prefix}/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @Value("${jwt.access-token-expiration-secs}")
    private Long accessTokenExpirationSecs;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), accessTokenExpirationSecs);

        return ResponseEntity.ok(new LoginResponse(accessToken, userDetails.getUsername()));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Validated RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(user.getUsername()));
    }
}
