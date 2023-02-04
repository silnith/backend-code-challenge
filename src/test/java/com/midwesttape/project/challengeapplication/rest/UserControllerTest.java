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
        
        assertNotNull(response);
    }
    
    @Test
    void user2() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/v1/users/2", null, null);
        final User response = restTemplate.getForObject(uri, User.class);
        
        assertNotNull(response);
    }
    
    @Test
    void userNotFound() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/v1/users/0", null, null);
        final ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}