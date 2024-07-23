package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.dtos.BrandDTO;
import com.dattran.productservice.domain.entities.Brand;
import com.dattran.productservice.domain.repositories.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;

    public Brand createBrand(BrandDTO brandDTO) {
        if (brandRepository.existsByName(brandDTO.getName())) {
            throw new AppException(ResponseStatus.BRAND_ALREADY_EXIST);
        }
        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .isDeleted(false)
                .images(brandDTO.getImages())
                .build();
        return brandRepository.save(brand);
    }
}
