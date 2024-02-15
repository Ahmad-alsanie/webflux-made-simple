package com.sanie.controller;

import com.sanie.model.Product;
import com.sanie.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable String id) {
        return repository.findById(id);
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        return repository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return repository.save(existingProduct);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return repository.deleteById(id);
    }
}
