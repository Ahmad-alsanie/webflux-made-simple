package com.sanie.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.time.LocalDateTime;

@RestController
public class StreamController {

    @GetMapping(value = "/stream-time", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTime() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> "Time now is: " + LocalDateTime.now());
    }
}
