package ru.draen.hps.common.webflux.config.auth;

import org.springframework.security.config.web.server.ServerHttpSecurity;

public interface RequestApplier {
    void apply(ServerHttpSecurity.AuthorizeExchangeSpec auth);
}
