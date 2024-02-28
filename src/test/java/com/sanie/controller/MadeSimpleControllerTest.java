package com.sanie.controller;

import com.sanie.configuration.WebSecurityConfig;
import com.sanie.fixture.TestAuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.Mockito.when;

@WebFluxTest(MadeSimpleController.class)
@Import(WebSecurityConfig.class)
public class MadeSimpleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveUserDetailsService userDetailsService;

    @Autowired
    private WebTestClient autowiredWebTestClient;



    @BeforeEach
    public void setup(){
        UserDetails mockUserDetails = new User(TestAuthUtils.USERNAME, TestAuthUtils.PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.findByUsername(TestAuthUtils.USERNAME)).thenReturn(Mono.just(mockUserDetails));
        this.webTestClient = TestAuthUtils.withBasicAuth(autowiredWebTestClient);
    }

    @Test
    void simpleEndpointReturnsExpectedValue() {
        webTestClient.get().uri("/simple")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("WebFlux, Made Simple!");
    }

    @Test
    void greetingsEndpointReturnsAllGreetings() {
        webTestClient.get().uri("/greetings")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class)
                .hasSize(3)
                .contains("Hello, World!", "Hola, Mundo!", "Bonjour, Monde!");
    }

    @Test
    void greetingsEndpointDelaysBetweenElements() {
        webTestClient.get().uri("/greetings")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .returnResult(String.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNext("Hello, World!")
                .expectNoEvent(Duration.ofSeconds(1)) // verify the delay between elements
                .expectNext("Hola, Mundo!")
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNext("Bonjour, Monde!")
                .thenCancel()
                .verify(Duration.ofSeconds(5)); // give it timeout
    }
}
