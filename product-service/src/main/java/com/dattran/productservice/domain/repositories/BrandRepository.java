package com.dattran.productservice.domain.repositories;

import com.dattran.productservice.domain.entities.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BrandRepository extends MongoRepository<Brand, String> {
    Optional<Brand> findByName(String Name);
    boolean existsByName(String Name);
}
