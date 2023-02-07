package com.midwesttape.project.challengeapplication.model;

/**
 * An exception denoting that a user could not be found.
 */
public class UserNotFoundException extends Exception {
    
    /**
     * Initial version.
     * 
     * <p>This is not intended to be serialized, but technically it could be.
     */
    private static final long serialVersionUID = 1L;
    
    private final Long userId;

    /**
     * Create a new exception.
     * 
     * @param userId the user ID
     */
    public UserNotFoundException(final Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the user ID that could not be found.
     * 
     * @return the missing user ID
     */
    public Long getUserId() {
        return userId;
    }

}
