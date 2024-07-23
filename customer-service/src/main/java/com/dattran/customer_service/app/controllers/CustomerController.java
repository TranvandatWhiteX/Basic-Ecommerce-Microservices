package com.dattran.customer_service.app.controllers;

import com.dattran.annotations.HasRoles;
import com.dattran.aspect.ValidateRoleAspect;
import com.dattran.customer_service.app.dtos.CustomerDTO;
import com.dattran.customer_service.app.dtos.FilterCustomerDTO;
import com.dattran.customer_service.app.dtos.UpdateCustomerDTO;
import com.dattran.customer_service.domain.entities.Customer;
import com.dattran.customer_service.domain.services.CustomerService;
import com.dattran.responses.ApiResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    @HasRoles(roles = {"CUSTOMER"})
    ApiResponse<Customer> getCustomer(@PathVariable("id") String customerId,
                                      @RequestHeader("X-Account-Id") String accountId,
                                      HttpServletRequest httpServletRequest) {
        Customer customer = customerService.getCustomer(customerId, accountId);
        return ApiResponse.<Customer>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(customer)
                .status(HttpStatus.OK)
                .message("Get customer Successfully!")
                .build();
    }

    @PutMapping("/{id}")
    @HasRoles(roles = {"CUSTOMER"})
    ApiResponse<Customer> updateCustomer(@PathVariable("id") String customerId,
                                         @RequestBody UpdateCustomerDTO customerDTO,
                                         HttpServletRequest httpServletRequest) {
        Customer customer = customerService.updateCustomer(customerId, customerDTO);
        return ApiResponse.<Customer>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(customer)
                .status(HttpStatus.OK)
                .message("Update customer Successfully!")
                .build();
    }

    @GetMapping
    @HasRoles(roles = {"ADMIN"})
    ApiResponse<Page<Customer>> getAllCustomers(@ModelAttribute FilterCustomerDTO filterCustomerDTO,
                                                HttpServletRequest httpServletRequest, Pageable pageable) {
        Page<Customer> customers = customerService.getAllCustomers(filterCustomerDTO, pageable);
        return ApiResponse.<Page<Customer>>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(customers)
                .status(HttpStatus.OK)
                .message("Get all customer Successfully!")
                .build();
    }
}