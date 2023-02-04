package com.midwesttape.project.challengeapplication.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import com.midwesttape.project.challengeapplication.model.Address;
import com.midwesttape.project.challengeapplication.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    
    @Value("${local.server.port}")
    int port;
    
    @Autowired
    TestRestTemplate restTemplate;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Test
    void user1() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/v1/users/1", null, null);
        final User response = restTemplate.getForObject(uri, User.class);
        
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
        assertEquals(expected, response);
    }
    
    @Test
    void user2() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/v1/users/2", null, null);
        final User response = restTemplate.getForObject(uri, User.class);

        final Address address = new Address();
        address.setAddress1("1590 Pennsylvania Ave.");
        address.setAddress2("Apt. 42");
        address.setCity("Washington");
        address.setState("District of Columbia");
        address.setPostal("20500");
        final User expected = new User();
        expected.setId(2L);
        expected.setFirstName("Anna");
        expected.setLastName("Conda");
        expected.setUsername("AnnaConda");
        expected.setPassword("Password234");
        expected.setAddress(address);
        assertEquals(expected, response);
    }
    
    @Test
    void userNotFound() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/v1/users/0", null, null);
        final ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    @DirtiesContext
    void createUser() throws URISyntaxException {
        final Address address = new Address();
        address.setAddress1("1590 Pennsylvania Ave.");
        address.setAddress2("Apt. 43");
        address.setCity("Washington");
        address.setState("District of Columbia");
        address.setPostal("20500");
        final User user = new User();
        user.setId(3L);
        user.setFirstName("Anna");
        user.setLastName("Gotta");
        user.setUsername("AnnaGotta");
        user.setPassword("Password345");
        user.setAddress(address);
        final URI uri = new URI("http", null, "localhost", port, "/v1/users", null, null);
        final ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<User>(user), String.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/v1/users/3", response.getHeaders().getLocation().getRawPath());
        
        // TODO: There has got to be a better way to do this.  Go find it.
        assertEquals("Anna", jdbcTemplate.queryForObject("select firstName from User where id = 3", String.class));
        assertEquals("Gotta", jdbcTemplate.queryForObject("select lastName from User where id = 3", String.class));
        assertEquals("AnnaGotta", jdbcTemplate.queryForObject("select username from User where id = 3", String.class));
        assertEquals("Password345", jdbcTemplate.queryForObject("select password from User where id = 3", String.class));
        assertEquals("1590 Pennsylvania Ave.", jdbcTemplate.queryForObject("select Address.address1 from Address inner join User on User.addressId = Address.id where User.id = 3", String.class));
        assertEquals("Apt. 43", jdbcTemplate.queryForObject("select Address.address2 from Address inner join User on User.addressId = Address.id where User.id = 3", String.class));
        assertEquals("Washington", jdbcTemplate.queryForObject("select Address.city from Address inner join User on User.addressId = Address.id where User.id = 3", String.class));
        assertEquals("District of Columbia", jdbcTemplate.queryForObject("select Address.state from Address inner join User on User.addressId = Address.id where User.id = 3", String.class));
        assertEquals("20500", jdbcTemplate.queryForObject("select Address.postal from Address inner join User on User.addressId = Address.id where User.id = 3", String.class));
    }
    
    @Test
    @DirtiesContext
    void updateUser() throws URISyntaxException {
        final Address address = new Address();
        address.setAddress1("Foo");
        address.setCity("Foo");
        address.setState("Foo");
        address.setPostal("Foo");
        final User user = new User();
        user.setId(4L);
        user.setFirstName("Foo");
        user.setLastName("Foo");
        user.setUsername("Foo");
        user.setPassword("Foo");
        user.setAddress(address);
        final URI uri = new URI("http", null, "localhost", port, "/v1/users", null, null);
        final ResponseEntity<String> createdResponse = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<User>(user), String.class);
        
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        
        address.setAddress1("Nowhere");
        address.setCity("Seattle");
        address.setState("Washington");
        address.setPostal("98101");
        user.setFirstName("Bradley");
        user.setLastName("APC");
        user.setUsername("Bob");
        user.setPassword("Nope");
        final ResponseEntity<String> updatedResponse = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<User>(user), String.class);
        
        assertEquals(HttpStatus.NO_CONTENT, updatedResponse.getStatusCode());
        
        assertEquals("Bradley", jdbcTemplate.queryForObject("select firstName from User where id = 4", String.class));
        assertEquals("APC", jdbcTemplate.queryForObject("select lastName from User where id = 4", String.class));
        assertEquals("Bob", jdbcTemplate.queryForObject("select username from User where id = 4", String.class));
        assertEquals("Nope", jdbcTemplate.queryForObject("select password from User where id = 4", String.class));
        assertEquals("Nowhere", jdbcTemplate.queryForObject("select Address.address1 from Address inner join User on User.addressId = Address.id where User.id = 4", String.class));
        assertEquals(null, jdbcTemplate.queryForObject("select Address.address2 from Address inner join User on User.addressId = Address.id where User.id = 4", String.class));
        assertEquals("Seattle", jdbcTemplate.queryForObject("select Address.city from Address inner join User on User.addressId = Address.id where User.id = 4", String.class));
        assertEquals("Washington", jdbcTemplate.queryForObject("select Address.state from Address inner join User on User.addressId = Address.id where User.id = 4", String.class));
        assertEquals("98101", jdbcTemplate.queryForObject("select Address.postal from Address inner join User on User.addressId = Address.id where User.id = 4", String.class));
    }

}
