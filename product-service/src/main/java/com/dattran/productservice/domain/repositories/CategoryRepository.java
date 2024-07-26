package com.dattran.productservice.domain.repositories;

import com.dattran.productservice.domain.entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByName(String name);
}
