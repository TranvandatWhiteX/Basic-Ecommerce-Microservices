package com.dattran.customer_service.domain.repositories;

import com.dattran.customer_service.app.dtos.FilterCustomerDTO;
import com.dattran.customer_service.domain.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterCustomerRepository {
    Page<Customer> filterCustomers(FilterCustomerDTO filterCustomerDTO, Pageable pageable);
}
