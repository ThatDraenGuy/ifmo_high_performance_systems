package ru.draen.hps.config.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import ru.draen.hps.I18n;
import ru.draen.hps.common.exception.AppException;
import ru.draen.hps.common.exception.TokenException;
import ru.draen.hps.common.label.ILabelService;

import java.io.FileReader;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;

public class JwtUtils {
    private static final ILabelService lbs = I18n.getLabelService();

    private static final String ISSUER = "HPS";
    private static final String TOKEN_TYPE_CLAIM = "token_type";

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

    public String generateToken(String subject, long expirationSecs, ETokenType tokenType) {
        return Jwts.builder()
                .subject(subject)
                .issuer(ISSUER)
                .expiration(Date.from(Instant.now().plusSeconds(expirationSecs)))
                .claim(TOKEN_TYPE_CLAIM, tokenType.name())
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public RefreshToken extractRefreshToken(String token) {
        Claims claims = extractClaims(token, ETokenType.REFRESH);
        return new RefreshToken(claims.getSubject());
    }

    public AccessToken extractAccessToken(String token) {
        Claims claims = extractClaims(token, ETokenType.ACCESS);
        return new AccessToken(claims.getSubject());
    }

    private Claims extractClaims(String token, ETokenType tokenType) {
        if (token.startsWith("Bearer ")) {
            token = token.replaceFirst("Bearer ", "");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parse(token)
                    .accept(Jws.CLAIMS)
                    .getPayload();

            if (!tokenType.name().equals(claims.get(TOKEN_TYPE_CLAIM))) {
                throw new TokenException(lbs.msg("TokenException.invalidTokenType"));
            }
            return claims;
        } catch (MalformedJwtException | SignatureException e) {
            throw new TokenException(lbs.msg("TokenException.invalidToken"), e);
        } catch (ExpiredJwtException e) {
            throw new TokenException(lbs.msg("TokenException.expiredToken"), e);
        }

    }

    public enum ETokenType {
        ACCESS,
        REFRESH
    }

    public record AccessToken(String username) {}

    public record RefreshToken(String username) {}
}