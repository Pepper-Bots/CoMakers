package io.github.pepperbots.comakers.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import io.github.pepperbots.comakers.auth.AuthService;
import java.time.Duration;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtConfig {
    @Bean
    public SecretKey jwtSigningKey(@Value("${comakers.jwt.secret}") String encodedSecret) {
        byte[] key;
        try {
            key = Base64.getDecoder().decode(encodedSecret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("JWT_SECRET doit être encodé en Base64.");
        }
        if (key.length < 32) {
            throw new IllegalStateException("JWT_SECRET doit contenir au moins 32 octets aléatoires.");
        }
        return new SecretKeySpec(key, "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSigningKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSigningKey));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSigningKey) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(jwtSigningKey)
                .macAlgorithm(MacAlgorithm.HS256).build();
        OAuth2TokenValidator<Jwt> requiredClaims = jwt -> {
            boolean validSubject;
            try {
                validSubject = jwt.getSubject() != null && Long.parseLong(jwt.getSubject()) > 0;
            } catch (NumberFormatException exception) {
                validSubject = false;
            }
            if (jwt.getExpiresAt() != null && jwt.getIssuedAt() != null && validSubject
                    && jwt.getAudience() != null && jwt.getAudience().contains(AuthService.AUDIENCE)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "Jeton invalide.", null));
        };
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ZERO),
                new JwtIssuerValidator(AuthService.ISSUER),
                requiredClaims));
        return decoder;
    }
}
