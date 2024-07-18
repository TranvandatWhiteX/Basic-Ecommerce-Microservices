package com.dattran.identity_service.domain.repositories;

import com.dattran.identity_service.app.dtos.CustomerDTO;
import com.dattran.identity_service.app.responses.ApiResponse;
import com.dattran.identity_service.domain.entities.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customer-service", url = "${services.customer}")
public interface CustomerClient {
    @PostMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Customer> createCustomer(@RequestBody CustomerDTO customerDTO);

    @PostMapping(value = "/customers/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Customer> verifyCustomer(@RequestBody String customerId);
}
