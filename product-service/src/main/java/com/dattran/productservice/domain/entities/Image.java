package com.dattran.productservice.domain.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @NotNull(message = "Url must not be null.")
    String url;

    @NotNull(message = "IsCover must not be null.")
    Boolean isCover;
}
