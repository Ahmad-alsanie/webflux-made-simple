package com.sanie.client;

import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;

@Component
public class DataStreamClient {

    private final WebClient webClient;

    public DataStreamClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public void consumeDataStreamWithBackpressure() {
        webClient.get().uri("/data-stream")
                .retrieve()
                .bodyToFlux(Integer.class)
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed");
                        request(10); // request the first 10 items
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("Received: " + value);
                        if (value % 10 == 0) { // every 10th item received, request 10 more items
                            try {
                                Thread.sleep(50); // simulate some processing delay
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            request(10);
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        System.out.println("Completed");
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        System.err.println("Error: " + throwable.getMessage());
                    }
                });
    }

}
