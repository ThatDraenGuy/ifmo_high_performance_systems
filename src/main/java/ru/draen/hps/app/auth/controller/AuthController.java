package ru.draen.hps.app.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.app.auth.controller.dto.LoginRequest;
import ru.draen.hps.app.auth.controller.dto.LoginResponse;
import ru.draen.hps.app.auth.controller.dto.RefreshRequest;
import ru.draen.hps.common.exception.TokenException;
import ru.draen.hps.config.AppProfile;
import ru.draen.hps.config.auth.JwtUtils;

@Profile(AppProfile.DEV)
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Value("${jwt.access-token-expiration-secs}")
    private Long accessTokenExpirationSecs;

    @Value("${jwt.refresh-token-expiration-secs}")
    private Long refreshTokenExpirationSecs;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String accessToken = jwtUtils.generateToken(userDetails.getUsername(), accessTokenExpirationSecs,
                JwtUtils.ETokenType.ACCESS);
        String refreshToken = jwtUtils.generateToken(userDetails.getUsername(), refreshTokenExpirationSecs,
                JwtUtils.ETokenType.REFRESH);

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, userDetails.getUsername()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Validated RefreshRequest refreshRequest) {
        String rawToken = refreshRequest.refreshToken();
        JwtUtils.RefreshToken refreshToken = jwtUtils.extractRefreshToken(rawToken);
        String username = refreshToken.getUsername();
        try {
            userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new TokenException("TokenException.userNonExistent", e);
        }

        String accessToken = jwtUtils.generateToken(username, accessTokenExpirationSecs,
                JwtUtils.ETokenType.ACCESS);
        String newRefreshToken = jwtUtils.generateToken(username, refreshTokenExpirationSecs,
                JwtUtils.ETokenType.REFRESH);

        return ResponseEntity.ok(new LoginResponse(accessToken, newRefreshToken, username));
    }
}
