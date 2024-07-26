package com.dattran.productservice.app.controllers;

import com.dattran.annotations.HasRoles;
import com.dattran.productservice.app.dtos.CategoryDTO;
import com.dattran.productservice.domain.entities.Brand;
import com.dattran.productservice.domain.entities.Category;
import com.dattran.productservice.domain.services.CategoryService;
import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    @HasRoles(roles = {"ADMIN"})
    public ApiResponse<Category> createCategory(@RequestBody CategoryDTO categoryDTO, HttpServletRequest httpServletRequest) {
        Category category = categoryService.createCategory(categoryDTO);
        return ApiResponse.<Category>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(category)
                .status(HttpStatus.CREATED)
                .message("Category Created Successfully!")
                .build();
    }
}
