package com.dattran.customer_service.domain.services;

import com.dattran.customer_service.app.dtos.CustomerDTO;
import com.dattran.customer_service.app.dtos.FilterCustomerDTO;
import com.dattran.customer_service.app.dtos.UpdateCustomerDTO;
import com.dattran.customer_service.domain.entities.Customer;
import com.dattran.customer_service.domain.enums.CustomerState;
import com.dattran.customer_service.domain.repositories.CustomerRepository;
import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByAccountId(customerDTO.getUserId())) {
            throw new AppException(ResponseStatus.ACCOUNT_HAS_ONE_PROFILE);
        }
        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .dob(customerDTO.getDob())
                .address(customerDTO.getAddress())
                .accountId(customerDTO.getUserId())
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

    public Customer getCustomer(String customerId, String accountId) {
       Customer customer = customerRepository.findById(customerId)
               .orElseThrow(()-> new AppException(ResponseStatus.CUSTOMER_NOT_FOUND));
       if (!customer.getAccountId().equals(accountId)) {
          throw new AppException(ResponseStatus.FORBIDDEN);
       }
        return customer;
    }

    public Page<Customer> getAllCustomers(FilterCustomerDTO filterCustomerDTO, Pageable pageable) {
        return customerRepository.filterCustomers(filterCustomerDTO, pageable);
    }

    public Customer updateCustomer(String customerId, UpdateCustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new AppException(ResponseStatus.CUSTOMER_NOT_FOUND));
        customer.setFullName(customerDTO.getFullName());
        customer.setDob(customerDTO.getDob());
        customer.setAddress(customerDTO.getAddress());
        return customerRepository.save(customer);
    }
}
