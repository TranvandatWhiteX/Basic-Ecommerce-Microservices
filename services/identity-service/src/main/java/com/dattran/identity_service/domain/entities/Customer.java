package com.dattran.identity_service.domain.entities;

import com.dattran.identity_service.domain.enums.CustomerState;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    String id;

    String fullName;

    String email;

    LocalDate dob;

    Address address;

    String userId;

    CustomerState customerState;

    Instant createdAt;

    Instant updatedAt;
}
