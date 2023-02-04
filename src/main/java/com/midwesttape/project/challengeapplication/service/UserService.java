package com.midwesttape.project.challengeapplication.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.midwesttape.project.challengeapplication.model.Address;
import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserNotFoundException;

/**
 * Data service for user information.
 */
@Service
public class UserService {

    private final DataSource dataSource;

    /**
     * Constructs a new instance.
     */
    public UserService(final DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

    /**
     * Returns a user's information.  Will throw {@link UserNotFoundException}
     * if no data is available for the given ID.
     * 
     * @param userId the user ID
     * @return the user details
     * @throws UserNotFoundException if no data is found for the given ID
     * @throws SQLException if there was a problem querying the database
     */
	public User user(final Long userId) throws UserNotFoundException, SQLException {
	    try (final Connection connection = dataSource.getConnection();
	            final PreparedStatement userQuery = connection.prepareStatement("select firstName, lastName, username, password, addressId from User where id = ?");
	            final PreparedStatement addressQuery = connection.prepareStatement("select address1, address2, city, state, postal from Address where id = ?");) {
	        userQuery.setLong(1, userId);
            
            try (final ResultSet userResultSet = userQuery.executeQuery();) {
                if (userResultSet.next()) {
                    final User user = new User();
                    user.setId(userId);
                    user.setFirstName(userResultSet.getString(1));
                    user.setLastName(userResultSet.getString(2));
                    user.setUsername(userResultSet.getString(3));
                    user.setPassword(userResultSet.getString(4));

                    final long addressId = userResultSet.getLong(5);
                    addressQuery.setLong(1, addressId);
                    
                    try (final ResultSet addressResultSet = addressQuery.executeQuery();) {
                        if (addressResultSet.next()) {
                            final Address address = new Address();
                            address.setId(addressId);
                            address.setAddress1(addressResultSet.getString(1));
                            address.setAddress2(addressResultSet.getString(2));
                            address.setCity(addressResultSet.getString(3));
                            address.setState(addressResultSet.getString(4));
                            address.setPostal(addressResultSet.getString(5));
                            user.setAddress(address);
                        }
                    }
                    
                    return user;
                } else {
                    throw new UserNotFoundException(userId);
                }
            }
        }

    }

}
