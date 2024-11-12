package ru.draen.hps.common.webflux.config.auth;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import ru.draen.hps.common.security.config.auth.JwtUtils;

@Getter
public class JwtTokenAuthentication extends AbstractAuthenticationToken {
    private final JwtUtils.AccessToken accessToken;

    public JwtTokenAuthentication(JwtUtils.AccessToken accessToken) {
        super(null);
        this.accessToken = accessToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
