package com.dattran.productservice.app.requests;

import com.dattran.productservice.app.dtos.ProductVariantDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantRequest {
    @NotNull(message = "Product Id must not be null.")
    String productId;

    List<ProductVariantDTO> productVariantDTOS;
}
