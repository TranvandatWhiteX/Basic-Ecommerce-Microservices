package com.dattran.customer_service.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    String province;

    String district;

    String ward;

    String street;

    String detail;
}
