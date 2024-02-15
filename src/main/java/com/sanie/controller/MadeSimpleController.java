package com.sanie.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
public class MadeSimpleController {

    @GetMapping("/simple")
    public Mono<String> simple() {
        return Mono.just("WebFlux, Made Simple!");
    }

    @GetMapping("/greetings")
    public Flux<String> greetings() {
        List<String> greetings = Arrays.asList("Hello, World!", "Hola, Mundo!", "Bonjour, Monde!");
        return Flux.fromIterable(greetings).delayElements(Duration.ofSeconds(1));
    }
}
