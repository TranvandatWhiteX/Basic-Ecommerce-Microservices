package com.dattran.productservice.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "product-variants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant extends BaseEntity {
    @Id
    String id;

    BigDecimal price;

    Long quantity;

    Map<String, String> attributes;

    Boolean isDeleted;

    @DBRef
    Product product;
}
