package io.github.pepperbots.comakers.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    UserRepository repository;
    BCryptPasswordEncoder encoder;
    UserService service;

    @BeforeEach
    void setup() {
        repository = mock(UserRepository.class);
        encoder = new BCryptPasswordEncoder();
        service = new UserService(repository, encoder);
    }

    @Test
    void normalizesIdentityAndStoresOnlyHashedPassword() {
        when(repository.saveAndFlush(any(User.class))).thenAnswer(call -> call.getArgument(0));
        User user = service.register(" Alice ", " Alice@Example.COM ", "password123");
        assertEquals("Alice", user.getDisplayName());
        assertEquals("alice@example.com", user.getEmail());
        assertNotEquals("password123", user.getPasswordHash());
        assertTrue(encoder.matches("password123", user.getPasswordHash()));
    }

    @Test
    void rejectsExistingEmailBeforeSaving() {
        when(repository.existsByEmail("alice@example.com")).thenReturn(true);
        assertThrows(EmailAlreadyUsedException.class,
                () -> service.register("Alice", "ALICE@example.com", "password123"));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void handlesConcurrentDuplicateUsingDatabaseConstraint() {
        when(repository.existsByEmail("alice@example.com")).thenReturn(false, true);
        when(repository.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("duplicate"));
        assertThrows(EmailAlreadyUsedException.class,
                () -> service.register("Alice", "alice@example.com", "password123"));
    }

    @Test
    void doesNotMislabelOtherDatabaseFailuresAsDuplicateEmails() {
        var failure = new DataIntegrityViolationException("other constraint");
        when(repository.saveAndFlush(any())).thenThrow(failure);
        assertSame(failure, assertThrows(DataIntegrityViolationException.class,
                () -> service.register("Alice", "alice@example.com", "password123")));
    }

    @Test
    void checksBcryptByteLimitForUnicodePasswords() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("Alice", "alice@example.com", "é".repeat(37)));
        verifyNoInteractions(repository);
    }
}
