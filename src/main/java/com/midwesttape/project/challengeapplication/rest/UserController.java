package com.midwesttape.project.challengeapplication.rest;

import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserNotFoundException;
import com.midwesttape.project.challengeapplication.service.UserService;

import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReST controller for the {@code  /v1/users} endpoint.
 */
@RestController
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new controller.
     * 
     * @param userService the user service
     */
    public UserController(final UserService userService) {
		super();
		this.userService = userService;
	}

    /**
     * Returns the details for a given user.
     * 
     * @param userId the user ID to look up
     * @return the user details
     * @throws UserNotFoundException if no user can be found for the given ID
     * @throws SQLException if there was a problem connecting to the database
     */
	@GetMapping("/v1/users/{userId}")
    public User user(@PathVariable final Long userId) throws UserNotFoundException, SQLException {
        return userService.user(userId);
    }

}
