package com.midwesttape.project.challengeapplication.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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
	public User queryUser(final Long userId) throws UserNotFoundException, SQLException {
	    try (final Connection connection = dataSource.getConnection();
	            final PreparedStatement userQuery = connection.prepareStatement("select firstName, lastName, username, password, addressId from User where id = ?");) {
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
                    if (userResultSet.wasNull()) {
                        // No address to query.
                    } else {
                        try (final PreparedStatement addressQuery = connection.prepareStatement("select address1, address2, city, state, postal from Address where id = ?");) {
                            addressQuery.setLong(1, addressId);

                            try (final ResultSet addressResultSet = addressQuery.executeQuery();) {
                                if (addressResultSet.next()) {
                                    final Address address = new Address();
                                    address.setAddress1(addressResultSet.getString(1));
                                    address.setAddress2(addressResultSet.getString(2));
                                    address.setCity(addressResultSet.getString(3));
                                    address.setState(addressResultSet.getString(4));
                                    address.setPostal(addressResultSet.getString(5));
                                    user.setAddress(address);
                                }
                            }
                        }
                    }
                    
                    return user;
                } else {
                    throw new UserNotFoundException(userId);
                }
            }
        }
    }
	
	/**
	 * Creates or updates the user as appropriate.
	 * 
	 * @param user the user to update
	 * @return {@code true} if the user was created, {@code false} if the user was updated
	 * @throws SQLException if there was a problem communicating with the database
	 */
	public boolean setUser(final User user) throws SQLException {
	    final boolean createdUser;
	    try (final Connection connection = dataSource.getConnection();
	            final PreparedStatement addressIdQuery = connection.prepareStatement("select addressId from User where id = ?");) {
	        connection.setAutoCommit(false);
            final Address address = user.getAddress();
	        
	        // First, find out whether the user exists already, and if so what
	        // the addressId is.
	        addressIdQuery.setLong(1, user.getId());
	        try (final ResultSet addressIdResult = addressIdQuery.executeQuery();) {
                if (addressIdResult.next()) {
	                // The user exists.
                    final Long previousAddressId;
                    {
                        final long addressId = addressIdResult.getLong(1);
                        if (addressIdResult.wasNull()) {
                            // The user does not currently have an address.
                            previousAddressId = null;
                        } else {
                            // The user has an existing address.  Update or delete it.
                            previousAddressId = addressId;
                        }
                    }
	                
	                // We need to insert, update, or delete the address as appropriate.
	                final Long newAddressId;
                    final boolean deleteAddress;
	                if (address == null) {
	                    // Remove the existing address, if any.
	                    // The User table has a foreign key constraint on the Address table.
	                    // Therefore, we need to update User before deleting from Address.
	                    deleteAddress = (previousAddressId != null);
	                    newAddressId = null;
	                } else {
	                    // Create or update the address.
	                    deleteAddress = false;
	                    if (previousAddressId == null) {
	                        // Create a new row.
	                        newAddressId = createAddressRow(connection, address);
	                    } else {
	                        // Just update the row in-place.
                            updateAddressRow(connection, previousAddressId, address);
	                        newAddressId = previousAddressId;
	                    }
	                }
	                
	                updateUserRow(connection, user, newAddressId);
                    
                    createdUser = false;
	                
	                if (deleteAddress) {
	                    assert previousAddressId != null;
	                    try (final PreparedStatement deleteAddressStatement = connection.prepareStatement("delete from Address where id = ?");) {
	                        deleteAddressStatement.setLong(1, previousAddressId);
	                        deleteAddressStatement.executeUpdate();
	                    }
	                }
	            } else {
	                // The user does not exist.
	                // Create address if appropriate.
	                final Long desiredAddressId;
	                if (address == null) {
	                    desiredAddressId = null;
	                } else {
	                    desiredAddressId = createAddressRow(connection, address);
	                }
	                // And then create the user, second in case the foreign key needs to exist.
	                createUserRow(connection, user, desiredAddressId);
	                createdUser = true;
	            }
	        }
	        
	        connection.commit();
	    }
	    return createdUser;
	}

    /**
     * Creates a new row in the User table.
     * 
     * @param connection the database connection, for transaction management
     * @param user the user information
     * @param desiredAddressId either the ID of a row in the Address table, or {@code null}
     * @throws SQLException if there was a problem communicating with the database
     */
    private void createUserRow(final Connection connection, final User user, final Long desiredAddressId)
            throws SQLException {
        try (final PreparedStatement insertUserStatement = connection.prepareStatement("insert into User (id, firstName, lastName, username, password, addressId) values (?, ?, ?, ?, ?, ?)");) {
            insertUserStatement.setLong(1, user.getId());
            insertUserStatement.setString(2, user.getFirstName());
            insertUserStatement.setString(3, user.getLastName());
            insertUserStatement.setString(4, user.getUsername());
            insertUserStatement.setString(5, user.getPassword());
            if (desiredAddressId == null) {
                insertUserStatement.setNull(6, Types.BIGINT);
            } else {
                insertUserStatement.setLong(6, desiredAddressId);
            }
            
            insertUserStatement.executeUpdate();
        }
    }

    /**
     * Updates an existing row in the User table.
     * 
     * @param connection the database connection, for transaction management
     * @param user the user information
     * @param desiredAddressId either the ID of a row in the Address table, or {@code null}
     * @throws SQLException if there was a problem communicating with the database
     */
    private void updateUserRow(final Connection connection, final User user, final Long desiredAddressId)
            throws SQLException {
        try (final PreparedStatement updateUserStatement = connection.prepareStatement("update User set firstName = ?, lastName = ?, username = ?, password = ?, addressId = ? where id = ?");) {
            updateUserStatement.setString(1, user.getFirstName());
            updateUserStatement.setString(2, user.getLastName());
            updateUserStatement.setString(3, user.getUsername());
            updateUserStatement.setString(4, user.getPassword());
            if (desiredAddressId == null) {
                updateUserStatement.setNull(5, Types.BIGINT);
            } else {
                updateUserStatement.setLong(5, desiredAddressId);
            }
            updateUserStatement.setLong(6, user.getId());
            
            updateUserStatement.executeUpdate();
        }
    }

    /**
     * Updates an existing address row in the database.
     * 
     * @param connection the database connection, for transaction management
     * @param addressId the primary key for the row
     * @param address the new values
     * @throws SQLException if there was a problem communicating with the database
     */
    private void updateAddressRow(final Connection connection, final long addressId, final Address address)
            throws SQLException {
        try (final PreparedStatement updateAddressStatement = connection.prepareStatement("update Address set address1 = ?, address2 = ?, city = ?, state = ?, postal = ? where id = ?");) {
            updateAddressStatement.setString(1, address.getAddress1());
            updateAddressStatement.setString(2, address.getAddress2());
            updateAddressStatement.setString(3, address.getCity());
            updateAddressStatement.setString(4, address.getState());
            updateAddressStatement.setString(5, address.getPostal());
            updateAddressStatement.setLong(6, addressId);
            
            updateAddressStatement.executeUpdate();
        }
    }

	/**
	 * Inserts a new row into the Address table, and returns the ID generated for the new row.
	 * 
     * @param connection the database connection, for transaction management
	 * @param address the address to insert
	 * @return the generated addressId
	 * @throws SQLException if there was a problem communicating with the database
	 */
    private long createAddressRow(final Connection connection, final Address address) throws SQLException {
        try (final PreparedStatement insertAddressStatement = connection.prepareStatement("insert into Address (address1, address2, city, state, postal) values (?, ?, ?, ?, ?)", new String[] {"id"});) {
            insertAddressStatement.setString(1, address.getAddress1());
            insertAddressStatement.setString(2, address.getAddress2());
            insertAddressStatement.setString(3, address.getCity());
            insertAddressStatement.setString(4, address.getState());
            insertAddressStatement.setString(5, address.getPostal());
            
            insertAddressStatement.executeUpdate();
            try (final ResultSet resultSet = insertAddressStatement.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                } else {
                    throw new RuntimeException("Failed to create new row in Address table.");
                }
            }
        }
    }

}
