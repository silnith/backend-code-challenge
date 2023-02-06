package com.midwesttape.project.challengeapplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.midwesttape.project.challengeapplication.model.Address;
import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserNotFoundException;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void should_get_user() throws UserNotFoundException, SQLException {
        final Address address = new Address();
        address.setAddress1("1600 Pennsylvania Ave.");
        address.setAddress2("c/o Fools");
        address.setCity("Washington");
        address.setState("District of Columbia");
        address.setPostal("20500");
        final User expected = new User();
        expected.setId(1L);
        expected.setFirstName("Phil");
        expected.setLastName("Ingwell");
        expected.setUsername("PhilIngwell");
        expected.setPassword("Password123");
        expected.setAddress(address);
        final User user = expected;

        final User resultUser = userService.queryUser(1L);

        assertEquals(user, resultUser);
    }

    @Test
    public void user_not_found() throws UserNotFoundException, SQLException {
        try {
            @SuppressWarnings("unused")
            final User resultUser = userService.queryUser(1234L);
            fail();
        } catch (final UserNotFoundException e) {
            assertEquals(1234L, e.getUserId());
        }
    }

}