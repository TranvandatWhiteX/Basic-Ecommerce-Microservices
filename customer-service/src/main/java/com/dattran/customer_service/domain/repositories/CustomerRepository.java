package com.dattran.customer_service.domain.repositories;

import com.dattran.customer_service.domain.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String>, FilterCustomerRepository {
    boolean existsByAccountId(String accountId);
}
