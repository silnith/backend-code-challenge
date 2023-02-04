package com.midwesttape.project.challengeapplication.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.midwesttape.project.challengeapplication.model.Address;
import com.midwesttape.project.challengeapplication.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    
    @Value("${local.server.port}")
    int port;
    
    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void user1() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/v1/users/1", null, null);
        final User response = restTemplate.getForObject(uri, User.class);
        
        final Address address = new Address();
        address.setId(1L);
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
        address.setId(2L);
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

}