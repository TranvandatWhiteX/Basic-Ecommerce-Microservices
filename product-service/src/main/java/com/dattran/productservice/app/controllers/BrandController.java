package com.dattran.productservice.app.controllers;

import com.dattran.productservice.app.dtos.BrandDTO;
import com.dattran.productservice.domain.entities.Brand;
import com.dattran.productservice.domain.services.BrandService;
import com.dattran.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @PostMapping
    public ApiResponse<Brand> createBrand(@RequestBody BrandDTO brandDTO) {
        return null;
    }
}
