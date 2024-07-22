package com.dattran.productservice.app.dtos;

import com.dattran.productservice.domain.entities.Image;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandDTO {
    String name;

    List<Image> images;
}
