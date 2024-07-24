package com.dattran.productservice.app.controllers;

import com.dattran.annotations.HasRoles;
import com.dattran.productservice.app.dtos.BrandDTO;
import com.dattran.productservice.domain.entities.Brand;
import com.dattran.productservice.domain.services.BrandService;
import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/brands")
public class BrandController {
    BrandService brandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @HasRoles(roles = {"ADMIN"})
    public ApiResponse<Brand> createBrand(@ModelAttribute BrandDTO brandDTO, HttpServletRequest httpServletRequest) {
        Brand brand  = brandService.createBrand(brandDTO);
        return ApiResponse.<Brand>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(brand)
                .status(HttpStatus.CREATED)
                .message("Brand Created Successfully!")
                .build();
    }
}
