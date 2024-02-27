package com.sanie.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

class GreetingHandlerTest {

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        GreetingHandler greetingHandler = new GreetingHandler();
        this.webTestClient = WebTestClient.bindToRouterFunction(RouterFunctions.route(GET("/simple-handler"), greetingHandler::hello))
                .build();
    }

    @Test
    void testHello() {
        webTestClient.get().uri("/simple-handler")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("WebFlux Made Simple - From Handler!");
    }
}
