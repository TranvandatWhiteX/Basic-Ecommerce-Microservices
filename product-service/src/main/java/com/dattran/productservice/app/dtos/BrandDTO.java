package com.dattran.productservice.app.dtos;

import com.dattran.productservice.domain.entities.Image;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandDTO {
    @NotNull(message = "Name must not be null.")
    String name;

    List<MultipartFile> files;

    @AssertTrue(message = "Images must have at least 1.")
    private boolean validateImage() {
        return !files.isEmpty();
    }
}
