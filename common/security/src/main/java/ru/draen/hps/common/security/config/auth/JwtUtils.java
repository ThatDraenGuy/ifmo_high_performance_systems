package ru.draen.hps.common.security.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import ru.draen.hps.common.core.exception.AppException;
import ru.draen.hps.common.core.exception.TokenException;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.security.I18n;

import java.io.FileReader;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;

public class JwtUtils {
    private static final ILabelService lbs = I18n.getLabelService();

    private static final String ISSUER = "HPS";

    private final KeyPair keyPair;

    public JwtUtils(String privateKeyPath) {
        try (PEMParser pemParser = new PEMParser(new FileReader(privateKeyPath))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object o = pemParser.readObject();
            this.keyPair = converter.getKeyPair((PEMKeyPair) o);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public String generateAccessToken(String subject, long expirationSecs) {
        return Jwts.builder()
                .subject(subject)
                .issuer(ISSUER)
                .expiration(Date.from(Instant.now().plusSeconds(expirationSecs)))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public AccessToken extractAccessToken(String token) {
        Claims claims = extractClaims(token);
        return new AccessToken(claims.getSubject());
    }

    private Claims extractClaims(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.replaceFirst("Bearer ", "");
        }

        try {
            return Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parse(token)
                    .accept(Jws.CLAIMS)
                    .getPayload();
        } catch (MalformedJwtException | SignatureException e) {
            throw new TokenException(lbs.msg("TokenException.invalidToken"), e);
        } catch (ExpiredJwtException e) {
            throw new TokenException(lbs.msg("TokenException.expiredToken"), e);
        }

    }

    public record AccessToken(String username) {}
}