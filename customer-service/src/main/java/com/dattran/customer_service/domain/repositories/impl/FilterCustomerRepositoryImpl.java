package com.dattran.customer_service.domain.repositories.impl;

import com.dattran.customer_service.app.dtos.FilterCustomerDTO;
import com.dattran.customer_service.domain.entities.Customer;
import com.dattran.customer_service.domain.repositories.FilterCustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilterCustomerRepositoryImpl implements FilterCustomerRepository {
    MongoTemplate mongoTemplate;

    @Override
    public Page<Customer> filterCustomers(FilterCustomerDTO filterCustomerDTO, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if (filterCustomerDTO.getFullName() != null && !filterCustomerDTO.getFullName().isEmpty()) {
            criteria.add(Criteria.where("fullName").regex(filterCustomerDTO.getFullName(), "i"));
        }
        if (filterCustomerDTO.getCustomerState() != null && !filterCustomerDTO.getCustomerState().isEmpty()) {
            criteria.add(Criteria.where("customerState").is(filterCustomerDTO.getCustomerState()));
        }
        if (filterCustomerDTO.getStartSearch() != null) {
            criteria.add(Criteria.where("createdAt").gte(filterCustomerDTO.getStartSearch()));
        }
        if (filterCustomerDTO.getEndSearch() != null) {
            criteria.add(Criteria.where("createdAt").lte(filterCustomerDTO.getEndSearch()));
        }
        if (filterCustomerDTO.getProvince() != null && !filterCustomerDTO.getProvince().isEmpty()) {
            criteria.add(Criteria.where("province").regex(filterCustomerDTO.getProvince(), "i"));
        }
        if (filterCustomerDTO.getDistrict() != null && !filterCustomerDTO.getDistrict().isEmpty()) {
            criteria.add(Criteria.where("district").regex(filterCustomerDTO.getDistrict(), "i"));
        }
        if (filterCustomerDTO.getWard() != null && !filterCustomerDTO.getWard().isEmpty()) {
            criteria.add(Criteria.where("ward").regex(filterCustomerDTO.getWard(), "i"));
        }
        if (filterCustomerDTO.getStreet() != null && !filterCustomerDTO.getStreet().isEmpty()) {
            criteria.add(Criteria.where("street").regex(filterCustomerDTO.getStreet(), "i"));
        }
        if (filterCustomerDTO.getDetail() != null && !filterCustomerDTO.getDetail().isEmpty()) {
            criteria.add(Criteria.where("detail").regex(filterCustomerDTO.getDetail(), "i"));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        long count = mongoTemplate.count(query, Customer.class);
        query.with(pageable);
        List<Customer> customers = mongoTemplate.find(query, Customer.class);
        return new PageImpl<>(customers, pageable, count);
    }
}
