package com.midwesttape.project.challengeapplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.midwesttape.project.challengeapplication.rest.UserController;

@SpringBootTest
class ChallengeApplicationTests {
    
    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
    }
    
    @Test
    void userController() {
        assertNotNull(userController);
    }

}
