package com.dattran.productservice.domain.repositories;

import com.dattran.productservice.domain.entities.ProductVariant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductVariantRepository extends MongoRepository<ProductVariant, String> {
}
