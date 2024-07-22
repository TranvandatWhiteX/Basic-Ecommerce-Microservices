package com.dattran.productservice.domain.repositories;

import com.dattran.productservice.domain.entities.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<Brand, String> {
}
