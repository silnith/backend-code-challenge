package com.midwesttape.project.challengeapplication.rest;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring controller advice on how to handle SQL errors.
 */
@RestControllerAdvice
public class SqlConnectionAdvice {
    
    private final Logger logger = LoggerFactory.getLogger(SqlConnectionAdvice.class);

    /**
     * Handles {@link SQLException}.
     * 
     * @param ex the exception caught
     * @return the exception message
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public String translateDatabaseException(final SQLException ex) {
        logger.error("SQL exception", ex);
        return ex.getLocalizedMessage();
    }

}
