package com.sanie.router;

import com.sanie.handler.GreetingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GreetingRouterTest {

    private WebTestClient webTestClient;
    private GreetingHandler greetingHandlerMock;

    @BeforeEach
    void setUp() {
        greetingHandlerMock = Mockito.mock(GreetingHandler.class);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(GreetingHandler.class, () -> greetingHandlerMock);
        context.register(GreetingRouter.class);
        context.refresh();

        RouterFunction<ServerResponse> routerFunction = context.getBean(RouterFunction.class);
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        when(greetingHandlerMock.hello(any())).thenReturn(ServerResponse.ok().bodyValue("Mocked Response"));
    }

    @Test
    void route() {
        webTestClient.get().uri("/simple-handler")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Mocked Response");
    }
}
