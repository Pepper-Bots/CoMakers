package io.github.pepperbots.comakers.user;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("Cette adresse email est déjà utilisée.");
    }
}
