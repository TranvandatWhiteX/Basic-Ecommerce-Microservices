package com.dattran.customer_service.app.controllers;

import com.dattran.customer_service.app.dtos.CustomerDTO;
import com.dattran.customer_service.app.responses.ApiResponse;
import com.dattran.customer_service.domain.entities.Customer;
import com.dattran.customer_service.domain.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/customers")
public class CustomerController {
    CustomerService customerService;

    @PostMapping
    ApiResponse<Customer> createCustomer(@RequestBody CustomerDTO customerDTO, HttpServletRequest httpServletRequest) {
        Customer customer = customerService.createCustomer(customerDTO);
        return ApiResponse.<Customer>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(customer)
                .status(HttpStatus.OK)
                .message("Create customer Successfully!")
                .build();
    }

    @PostMapping("/verify")
    ApiResponse<Customer> verifyCustomer(@RequestBody String customerId, HttpServletRequest httpServletRequest) {
        Customer customer = customerService.verifyCustomer(customerId);
        return ApiResponse.<Customer>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(customer)
                .status(HttpStatus.OK)
                .message("Verify customer Successfully!")
                .build();
    }
}
