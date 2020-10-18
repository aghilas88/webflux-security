package com.agh.webfluxmongo.controllers;

import com.agh.webfluxmongo.dto.ProductDto;
import com.agh.webfluxmongo.model.CreationRequest;
import com.agh.webfluxmongo.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Date;

@RestController
@Log4j2
public class ProductController {

    final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/infos")
    public Mono<ResponseEntity<String>> infos(){
        log.info("infos");
        return Mono.just(ResponseEntity.ok("product service ok"));
    }

    @GetMapping("/products")
    public Flux<ProductDto> getAllProducts() {
        log.info("getAllProducts");
        return productRepository.findAll();
    }

    @PostMapping("/products")
    public Mono<ProductDto> createProduct(@RequestBody CreationRequest creationRequest) {
        log.info("createProduct");
        return productRepository.save(ProductDto.builder()
                .name(creationRequest.getName())
                .description(creationRequest.getDescription())
                .createdAt(new Date()).build());
    }

    @GetMapping("/products/{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable(value = "id") String productId) {
        log.info("getProductById - Authenticated user : ");
        return productRepository.findById(productId)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Products are sent to the client as Server Sent Events
    @GetMapping(value = "/stream/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> streamAllTweets() {
        log.info("streamAllTweets");
        return productRepository.findAll();
    }

}
