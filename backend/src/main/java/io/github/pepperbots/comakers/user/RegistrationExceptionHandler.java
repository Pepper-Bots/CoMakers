package io.github.pepperbots.comakers.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
public class RegistrationExceptionHandler {
    @ExceptionHandler(EmailAlreadyUsedException.class)
    ProblemDetail duplicateEmail(EmailAlreadyUsedException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class,
            IllegalArgumentException.class})
    ProblemDetail invalidRequest(Exception exception) {
        // Never return rejected values: they may include a password.
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Données d'inscription invalides. Vérifiez le nom, l'email et le mot de passe (8 caractères minimum, 72 octets maximum).");
    }
}
