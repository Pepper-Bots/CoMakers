package io.github.pepperbots.comakers.user;

import java.time.LocalDateTime;

public record UserResponse(Long id, String displayName, String email, LocalDateTime createdAt) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getDisplayName(), user.getEmail(), user.getCreatedAt());
    }
}
