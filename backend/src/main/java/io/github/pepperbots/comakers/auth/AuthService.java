package io.github.pepperbots.comakers.auth;

import io.github.pepperbots.comakers.user.User;
import io.github.pepperbots.comakers.user.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public static final String ISSUER = "comakers";
    public static final String AUDIENCE = "comakers-api";
    public static final long TOKEN_LIFETIME_SECONDS = 900;

    private final UserRepository users;
    private final PasswordEncoder passwords;
    private final JwtEncoder jwtEncoder;
    private final String dummyHash;

    public AuthService(UserRepository users, PasswordEncoder passwords, JwtEncoder jwtEncoder) {
        this.users = users;
        this.passwords = passwords;
        this.jwtEncoder = jwtEncoder;
        // Perform BCrypt verification even when the account does not exist.
        this.dummyHash = passwords.encode(UUID.randomUUID().toString());
    }

    public LoginResponse login(String email, String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw invalidCredentials();
        }
        User user = users.findByEmail(email.strip().toLowerCase(Locale.ROOT)).orElse(null);
        boolean matches = passwords.matches(password, user == null ? dummyHash : user.getPasswordHash());
        if (user == null || !matches) {
            throw invalidCredentials();
        }
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .audience(java.util.List.of(AUDIENCE))
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TOKEN_LIFETIME_SECONDS))
                .id(UUID.randomUUID().toString())
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new LoginResponse(token, "Bearer", TOKEN_LIFETIME_SECONDS);
    }

    static BadCredentialsException invalidCredentials() {
        return new BadCredentialsException("Email ou mot de passe incorrect.");
    }
}
