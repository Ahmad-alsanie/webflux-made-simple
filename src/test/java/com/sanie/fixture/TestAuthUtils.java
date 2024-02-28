package com.sanie.fixture;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestAuthUtils {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    public static WebTestClient withBasicAuth(WebTestClient client) {
        String encodedCredentials = Base64.getEncoder().encodeToString(
                (USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8)
        );

        return client.mutate()
                .defaultHeader("Authorization", "Basic " + encodedCredentials)
                .build();
    }
}

