package com.midwesttape.project.challengeapplication.model;

import java.util.Objects;

/**
 * The data model representing a single user.
 * 
 * <p>This corresponds to the database table "User".
 */
public class User {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public User() {
        super();
    }

    /**
     * Returns the user identifier.  This is generally a positive integer, but
     * is only guaranteed to not be null.
     * 
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user identifier.  May not be null.
     * 
     * @param id the user ID
     * @throws IllegalArgumentException if the ID is null
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the first name of the user.  Cannot be null.  May be empty.
     * 
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.  Cannot be null.  May be empty.
     * 
     * @param firstName the user's first name
     * @throws IllegalArgumentException if the name is null
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.  Cannot be null.  May be empty.
     * 
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.  Cannot be null.  May be empty.
     * 
     * @param lastName the user's last name
     * @throws IllegalArgumentException if the name is null
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the username of the user.  Cannot be null.  Must be unique.
     * 
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.  Cannot be null.  Must be unique.
     * 
     * @param username the user's username
     * @throws IllegalArgumentException if the name is null
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.  Cannot be null.  May be empty.
     * Will not be encrypted or protected in any way.
     * 
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.  Cannot be null.  May be empty.
     * Must be in plaintext.
     * 
     * @param password the user's password
     * @throws IllegalArgumentException if the password is null
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, id, lastName, password, username);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
                && Objects.equals(lastName, other.lastName) && Objects.equals(password, other.password)
                && Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
                + ", password=" + password + "]";
    }
}
