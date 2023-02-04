package com.midwesttape.project.challengeapplication.rest;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserNotFoundException;
import com.midwesttape.project.challengeapplication.service.UserService;

/**
 * ReST controller for the {@code /v1/users} endpoint.
 */
@RestController
@RequestMapping("/v1/users")
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
	@GetMapping("{userId}")
    public User getUser(@PathVariable final Long userId) throws UserNotFoundException, SQLException {
        return userService.queryUser(userId);
    }
	
	/**
	 * Allows clients to {@code PUT} a user into the system.  This will create
	 * or update the user as appropriate, depending on if a user with the given
	 * ID already exists or not.
	 * 
	 * @param user the user to record
	 * @param builder a helper for constructing the location header
	 * @return nothing, just an HTTP status code
	 * @throws SQLException if there was a problem connecting to the database
	 */
	@PutMapping
	public ResponseEntity<String> createOrUpdateUser(@RequestBody final User user, final UriComponentsBuilder builder) throws SQLException {
	    final boolean created = userService.setUser(user);
	    if (created) {
	        builder.pathSegment("v1", "users", "{userId}");
	        return ResponseEntity.created(builder.build(user.getId())).build();
	    } else {
	        return ResponseEntity.noContent().build();
	    }
	}

}
