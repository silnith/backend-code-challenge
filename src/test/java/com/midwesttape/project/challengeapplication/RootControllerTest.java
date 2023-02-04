package com.midwesttape.project.challengeapplication;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RootControllerTest {
    
    @Value("${local.server.port}")
    int port;
    
    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void index() throws URISyntaxException {
        final URI uri = new URI("http", null, "localhost", port, "/", null, null);
        final ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final MediaType contentType = response.getHeaders().getContentType();
        assertTrue(contentType.equalsTypeAndSubtype(MediaType.TEXT_HTML));
    }

}
