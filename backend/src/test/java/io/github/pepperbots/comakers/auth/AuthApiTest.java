package io.github.pepperbots.comakers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pepperbots.comakers.security.*;
import io.github.pepperbots.comakers.user.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuthController.class, UserController.class},
        properties = "comakers.jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@Import({SecurityConfig.class, JwtConfig.class, PasswordConfig.class, AuthService.class, UserService.class})
class AuthApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired PasswordEncoder passwords;
    @Autowired JwtEncoder encoder;
    @Autowired JwtDecoder decoder;
    @MockitoBean UserRepository users;
    User alice;

    @BeforeEach
    void setup() {
        alice = new User();
        ReflectionTestUtils.setField(alice, "id", 42L);
        alice.setDisplayName("Alice");
        alice.setEmail("alice@example.com");
        alice.setPasswordHash(passwords.encode("password123"));
        when(users.findByEmail("alice@example.com")).thenReturn(Optional.of(alice));
        when(users.findById(42L)).thenReturn(Optional.of(alice));
    }

    String loginBody(String email, String password) throws Exception {
        return json.writeValueAsString(new LoginRequest(email, password));
    }

    String login() throws Exception {
        var result = mvc.perform(post("/api/auth/login").contentType("application/json")
                .content(loginBody("ALICE@example.com", "password123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(900))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();
        assertNull(result.getRequest().getSession(false));
        return json.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    String signedToken(String issuer, List<String> audience, String subject, Instant expiry) {
        var claims = JwtClaimsSet.builder().issuer(issuer).audience(audience).subject(subject)
                .issuedAt(Instant.now().minusSeconds(120));
        if (expiry != null) claims.expiresAt(expiry);
        return encoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims.build())).getTokenValue();
    }

    @Test
    void loginTokenGivesAccessToCurrentUserWithoutExposingCredentials() throws Exception {
        String token = login();
        var jwt = decoder.decode(token);
        assertEquals("42", jwt.getSubject());
        assertEquals(900, jwt.getExpiresAt().getEpochSecond() - jwt.getIssuedAt().getEpochSecond());
        assertFalse(jwt.getClaims().containsKey("password"));
        assertFalse(jwt.getClaims().containsKey("passwordHash"));
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void newlyRegisteredUserCanLogin() throws Exception {
        when(users.saveAndFlush(any(User.class))).thenAnswer(call -> {
            User user = call.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 73L);
            when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(users.findById(73L)).thenReturn(Optional.of(user));
            return user;
        });
        mvc.perform(post("/api/users/register").contentType("application/json")
                .content("""
                        {"displayName":"Bob","email":"bob@example.com","password":"password123"}
                        """)).andExpect(status().isCreated());
        var result = mvc.perform(post("/api/auth/login").contentType("application/json")
                .content(loginBody("bob@example.com", "password123")))
                .andExpect(status().isOk()).andReturn();
        String token = json.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(73));
    }

    @Test
    void unknownEmailAndWrongPasswordReturnSameGenericError() throws Exception {
        String wrongPassword = mvc.perform(post("/api/auth/login").contentType("application/json")
                .content(loginBody("alice@example.com", "wrong-password")))
                .andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();
        String unknownEmail = mvc.perform(post("/api/auth/login").contentType("application/json")
                .content(loginBody("unknown@example.com", "password123")))
                .andExpect(status().isUnauthorized()).andReturn().getResponse().getContentAsString();
        assertEquals(wrongPassword, unknownEmail);
        assertFalse(wrongPassword.contains("wrong-password"));
    }

    @Test
    void rejectsInvalidInput() throws Exception {
        mvc.perform(post("/api/auth/login").contentType("application/json")
                .content(loginBody("invalid", ""))).andExpect(status().isBadRequest());
        verifyNoInteractions(users);
    }

    @Test
    void rejectsMalformedJson() throws Exception {
        mvc.perform(post("/api/auth/login").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(users);
    }

    @Test
    void rejectsPasswordsBeyondBcryptByteLimit() throws Exception {
        mvc.perform(post("/api/auth/login").contentType("application/json")
                .content(loginBody("alice@example.com", "é".repeat(37))))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(users);
    }

    @Test
    void rejectsMissingAndMalformedTokens() throws Exception {
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer garbage"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(users);
    }

    @Test
    void rejectsTamperedSignature() throws Exception {
        String token = login();
        int signatureStart = token.lastIndexOf('.') + 1;
        char replacement = token.charAt(signatureStart) == 'A' ? 'B' : 'A';
        String tampered = token.substring(0, signatureStart) + replacement + token.substring(signatureStart + 1);
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + tampered))
                .andExpect(status().isUnauthorized());
        verify(users, never()).findById(anyLong());
    }

    @Test
    void rejectsExpiredToken() throws Exception {
        String token = signedToken(AuthService.ISSUER, List.of(AuthService.AUDIENCE), "42",
                Instant.now().minusSeconds(60));
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsWrongIssuerAndAudienceAndInvalidSubjectAndMissingExpiry() throws Exception {
        for (String token : List.of(
                signedToken("other", List.of(AuthService.AUDIENCE), "42", Instant.now().plusSeconds(60)),
                signedToken(AuthService.ISSUER, List.of("other"), "42", Instant.now().plusSeconds(60)),
                signedToken(AuthService.ISSUER, List.of(AuthService.AUDIENCE), "invalid", Instant.now().plusSeconds(60)),
                signedToken(AuthService.ISSUER, List.of(AuthService.AUDIENCE), "42", null))) {
            mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                    .andExpect(status().isUnauthorized());
        }
        verifyNoInteractions(users);
    }

    @Test
    void deletedUserCannotAccessProfile() throws Exception {
        String token = login();
        when(users.findById(42L)).thenReturn(Optional.empty());
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginDoesNotAuthenticateLaterRequestsWithoutBearerToken() throws Exception {
        login();
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void otherRoutesRemainClosedEvenWithValidToken() throws Exception {
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + login()))
                .andExpect(status().isForbidden());
    }

    @Test
    void refusesInvalidSigningConfiguration() {
        JwtConfig config = new JwtConfig();
        assertThrows(IllegalStateException.class, () -> config.jwtSigningKey("not-base64!"));
        assertThrows(IllegalStateException.class, () -> config.jwtSigningKey("YWJj"));
    }
}
