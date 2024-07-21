package com.dattran.customer_service.domain.services;

import com.dattran.customer_service.app.dtos.CustomerDTO;
import com.dattran.customer_service.domain.entities.Customer;
import com.dattran.customer_service.domain.enums.CustomerState;
import com.dattran.customer_service.domain.enums.ResponseStatus;
import com.dattran.customer_service.domain.exceptions.AppException;
import com.dattran.customer_service.domain.repositories.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .dob(customerDTO.getDob())
                .email(customerDTO.getEmail())
                .address(customerDTO.getAddress())
                .userId(customerDTO.getUserId())
                .customerState(CustomerState.PENDING)
                .build();
        return customerRepository.save(customer);
    }

    public Customer verifyCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new AppException(ResponseStatus.CUSTOMER_NOT_FOUND));
        customer.setCustomerState(CustomerState.VERIFIED);
        return customerRepository.save(customer);
    }

    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(()-> new AppException(ResponseStatus.CUSTOMER_NOT_FOUND));
    }
}
