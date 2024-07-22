package com.dattran.productservice.domain.repositories;

import com.dattran.productservice.domain.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
