package ru.draen.hps.common.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.draen.hps.common.security.config.auth.JwtUtils;

@Profile(AppProfile.DEV)
@Configuration
public class CommonSecurityConfiguration {

    @Bean
    public JwtUtils jwtUtils(@Value("${jwt.private-key-path}") String privateKeyPath) {
        return new JwtUtils(privateKeyPath);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                HPS-ADMIN > HPS-USER-CHANGE
                HPS-USER-Change > HPS-USER-Get
                """);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder encoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        encoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
        return encoder;
    }
}
