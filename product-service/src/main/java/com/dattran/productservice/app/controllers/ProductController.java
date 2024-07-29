package com.dattran.productservice.app.controllers;

import com.dattran.annotations.HasRoles;
import com.dattran.productservice.app.dtos.ProductDTO;
import com.dattran.productservice.app.dtos.ProductVariantDTO;
import com.dattran.productservice.domain.entities.Category;
import com.dattran.productservice.domain.entities.Product;
import com.dattran.productservice.domain.services.ProductService;
import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @HasRoles(roles = {"ADMIN"})
    public ApiResponse<Product> createProduct(@ModelAttribute @Valid ProductDTO productDTO, HttpServletRequest httpServletRequest) {
        Product product = productService.createProduct(productDTO);
        return ApiResponse.<Product>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(product)
                .status(HttpStatus.CREATED)
                .message("Product Created Successfully!")
                .build();
    }
}
