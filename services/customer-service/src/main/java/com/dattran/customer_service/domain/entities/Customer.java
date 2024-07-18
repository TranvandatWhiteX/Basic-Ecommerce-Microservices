package com.dattran.customer_service.domain.entities;

import com.dattran.customer_service.domain.enums.CustomerState;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    String id;

    String fullName;

    String email;

    LocalDate dob;

    Address address;

    String userId;

    CustomerState customerState;
}
