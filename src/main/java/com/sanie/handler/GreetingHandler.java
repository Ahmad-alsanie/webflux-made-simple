package com.sanie.handler;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class GreetingHandler {
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("WebFlux Made Simple - From Handler!"), String.class);
    }
}

