package io.github.pepperbots.comakers.user;

import io.github.pepperbots.comakers.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean UserService service;

    @Test
    void registersWithoutAuthenticationAndDoesNotExposePasswordOrCreateSession() throws Exception {
        User user = new User();
        user.setDisplayName("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("sensitive-hash");
        when(service.register("Alice", "alice@example.com", "password123")).thenReturn(user);
        var result = mvc.perform(post("/api/users/register").contentType("application/json")
                .content("""
                        {"displayName":"Alice","email":"alice@example.com","password":"password123"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn();
        org.junit.jupiter.api.Assertions.assertNull(result.getRequest().getSession(false));
    }

    @Test
    void invalidFieldsDoNotReachServiceOrLeakPassword() throws Exception {
        mvc.perform(post("/api/users/register").contentType("application/json")
                .content("""
                        {"displayName":" ","email":"invalid","password":"short"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("short"))));
        verifyNoInteractions(service);
    }

    @Test
    void rejectsMalformedJson() throws Exception {
        mvc.perform(post("/api/users/register").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {
        when(service.register(anyString(), anyString(), anyString())).thenThrow(new EmailAlreadyUsedException());
        mvc.perform(post("/api/users/register").contentType("application/json")
                .content("""
                        {"displayName":"Alice","email":"alice@example.com","password":"password123"}
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    void otherEndpointsRemainClosed() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isForbidden());
    }
}
