package com.dattran.productservice.app.dtos;

import com.dattran.productservice.domain.entities.Image;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantDTO {
    @NotNull(message = "Price must not be null.")
    BigDecimal price;

    @NotNull(message = "Quantity must not be null.")
    Long quantity;

    Map<String, String> attributes;
}
