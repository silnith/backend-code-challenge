package com.midwesttape.project.challengeapplication.rest;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.midwesttape.project.challengeapplication.model.UserNotFoundException;

/**
 * Spring controller advice for how to handle {@link UserNotFoundException}.
 */
@RestControllerAdvice
public class UserNotFoundAdvice {

    /**
     * Converts {@link UserNotFoundException} into {@link HttpStatus#NOT_FOUND}.
     * 
     * @param ex the exception caught
     * @return the exception message
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String userNotFound(final UserNotFoundException ex) {
        return String.format(Locale.US, "User ID not found: %s", ex.getUserId());
    }

}
