package com.sanie.client;

import com.sanie.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataStreamClientTest {

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    private DataStreamClient dataStreamClient;

    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilderMock = Mockito.mock(WebClient.Builder.class, RETURNS_DEEP_STUBS);
        WebClient webClientMock = Mockito.mock(WebClient.class);
        when(webClientBuilderMock.baseUrl(anyString())).thenReturn(webClientBuilderMock);
        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        lenient().when(webClientMock.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(webClientMock.post()).thenReturn(requestBodyUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        lenient().when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        lenient().doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(any(Product.class));
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToMono(Product.class)).thenReturn(Mono.just(new Product("1", "Test Product", 9.99)));
        dataStreamClient = new DataStreamClient(webClientBuilderMock);
    }

    @Test
    void getAllProducts_Success() {
        when(responseSpec.bodyToFlux(Product.class)).thenReturn(Flux.just(new Product("1", "Product A", 99.99), new Product("2", "Product B", 199.99)));

        StepVerifier.create(dataStreamClient.getAllProducts())
                .expectNextMatches(product -> product.getId().equals("1") && product.getName().equals("Product A"))
                .expectNextMatches(product -> product.getId().equals("2") && product.getName().equals("Product B"))
                .verifyComplete();
    }

    @Test
    void createProduct_Success() {
        Product newProduct = new Product("1", "New Product", 299.99);
        when(responseSpec.bodyToMono(Product.class)).thenReturn(Mono.just(newProduct));

        StepVerifier.create(dataStreamClient.createProduct(newProduct))
                .expectNextMatches(product -> product.getId().equals("1") && product.getName().equals("New Product"))
                .verifyComplete();
    }

    @Test
    void getProduct_Found() {
        Product foundProduct = new Product("1", "Found Product", 399.99);
        when(responseSpec.bodyToMono(Product.class)).thenReturn(Mono.just(foundProduct));

        StepVerifier.create(dataStreamClient.getProduct("1"))
                .expectNextMatches(product -> product.getId().equals("1") && product.getName().equals("Found Product"))
                .verifyComplete();
    }

    @Test
    void getProduct_NotFound() {
        when(responseSpec.bodyToMono(Product.class)).thenReturn(Mono.error(new RuntimeException("Not found")));

        StepVerifier.create(dataStreamClient.getProduct("unknown"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Not found"))
                .verify();
    }

}
