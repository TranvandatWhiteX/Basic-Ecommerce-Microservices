package com.dattran.productservice.app.dtos;

import com.dattran.productservice.domain.entities.Image;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    @NotNull(message = "Name must be not null.")
    String name;

    String description;

    @NotNull(message = "Category Id must be not null.")
    String categoryId;

    Map<String, String> info;

    @NotNull(message = "Quantity must be not null.")
    Long quantity;

    @NotNull(message = "Price must be not null.")
    BigDecimal price;

    List<Image> images;

    List<ProductVariantDTO> productVariants;
}
