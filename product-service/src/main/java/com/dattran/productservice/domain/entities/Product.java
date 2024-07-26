package com.dattran.productservice.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Document(collection = "products")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Id
    String id;

    String name;

    String description;

    Map<String, String> info;

    List<Image> images;

    Long quantity;

    BigDecimal minPrice;

    BigDecimal maxPrice;

    Boolean isDeleted;

    @DBRef
    Category category;

    @DBRef
    List<ProductVariant> productVariants;
}
