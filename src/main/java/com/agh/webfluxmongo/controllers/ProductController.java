package com.agh.webfluxmongo.controllers;

import com.agh.webfluxmongo.dto.ProductDto;
import com.agh.webfluxmongo.model.CreationRequest;
import com.agh.webfluxmongo.repository.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Date;
import org.slf4j.Logger;

@RestController
public class ProductController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/infos")
    public Mono<ResponseEntity<String>> infos(Principal principal){
        log.info("getAllProducts - Authenticated user : " + principal.getName());
        return Mono.just(ResponseEntity.ok("product service ok, you are signed in as " + principal.getName()));
    }

    @GetMapping("/products")
    public Flux<ProductDto> getAllProducts(Principal principal) {
        log.info("getAllProducts - Authenticated user : " + principal.getName());
        return productRepository.findAll();
    }

    @PostMapping("/products")
    public Mono<ProductDto> createProduct(@RequestBody CreationRequest creationRequest, Principal principal) {
        log.info("createProduct - Authenticated user : " + principal.getName());
        return productRepository.save(ProductDto.builder()
                .name(creationRequest.getName())
                .description(creationRequest.getDescription())
                .createdAt(new Date()).build());
    }

    @GetMapping("/products/{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable(value = "id") String productId, Principal principal) {
        log.info("getProductById - Authenticated user : " + principal.getName());
        return productRepository.findById(productId)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Products are sent to the client as Server Sent Events
    @GetMapping(value = "/stream/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> streamAllTweets(Principal principal) {
        log.info("streamAllTweets - Authenticated user : " + principal.getName());
        return productRepository.findAll();
    }

}
