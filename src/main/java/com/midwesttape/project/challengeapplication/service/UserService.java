package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.User;
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
     * Returns a user's information.  Will return null
     * if no data is available for the given ID.
     * 
     * @param userId the user ID
     * @return the user details
     */
	public User user(final Long userId) {
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
            return null;
        }

    }

}
