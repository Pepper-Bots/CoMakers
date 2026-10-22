package io.github.pepperbots.comakers.auth;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {
}
