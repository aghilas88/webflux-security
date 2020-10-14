package com.agh.webfluxmongo.repository;

import com.agh.webfluxmongo.dto.ProductDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductDto, String> {
}
