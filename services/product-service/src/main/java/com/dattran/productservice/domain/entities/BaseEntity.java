package com.dattran.productservice.domain.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
public abstract class BaseEntity {
    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;
}
