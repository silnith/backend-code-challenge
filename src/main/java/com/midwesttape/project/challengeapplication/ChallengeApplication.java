package com.midwesttape.project.challengeapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The primary runnable for the REST service.
 */
@SpringBootApplication
public class ChallengeApplication {

    /**
     * The main application entry point.
     * 
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }

}
