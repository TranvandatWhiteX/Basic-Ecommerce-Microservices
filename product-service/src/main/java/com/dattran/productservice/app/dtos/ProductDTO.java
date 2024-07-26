package com.dattran.productservice.app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

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

    String info;

    @NotNull(message = "Quantity must be not null.")
    Long quantity;

    @NotNull(message = "Price must be not null.")
    BigDecimal price;

    List<MultipartFile> images;
}
