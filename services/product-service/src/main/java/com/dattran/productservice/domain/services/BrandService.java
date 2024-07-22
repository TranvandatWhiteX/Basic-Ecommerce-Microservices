package com.dattran.productservice.domain.services;

import com.dattran.productservice.app.dtos.BrandDTO;
import com.dattran.productservice.domain.entities.Brand;
import com.dattran.productservice.domain.repositories.BrandRepository;
import com.dattran.projectcommon.exceptions.AppException;
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
        return null;
    }
}
