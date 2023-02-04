package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserNotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Data service for user information.
 */
@Service
public class UserService {

    private final JdbcTemplate template;

    /**
     * Constructs a new instance.
     * 
     * @param template a JDBC template
     */
    public UserService(final JdbcTemplate template) {
		super();
		this.template = template;
	}

    /**
     * Returns a user's information.  Will throw {@link UserNotFoundException}
     * if no data is available for the given ID.
     * 
     * @param userId the user ID
     * @return the user details
     * @throws UserNotFoundException if no data is found for the given ID
     */
	public User user(final Long userId) throws UserNotFoundException {
        try {

            return template.queryForObject(
                "select " +
                    "id, " +
                    "firstName, " +
                    "lastName, " +
                    "username, " +
                    "password " +
                    "from User " +
                    "where id = ?",
                new BeanPropertyRowMapper<>(User.class),
                userId
            );
        } catch (final EmptyResultDataAccessException e) {
            throw new UserNotFoundException(userId);
        }

    }

}
