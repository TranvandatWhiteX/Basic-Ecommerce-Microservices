package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.dtos.CategoryDTO;
import com.dattran.productservice.domain.entities.Category;
import com.dattran.productservice.domain.repositories.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;

    public Category createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new AppException(ResponseStatus.CATEGORY_NOT_FOUND);
        }
        Category category = Category.builder()
                .isDeleted(false)
                .name(categoryDTO.getName())
                .build();
        category.setLevel(category.getLevel());
        if (categoryDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new AppException(ResponseStatus.CATEGORY_NOT_FOUND));
            category.setParentCategory(parentCategory);
        }
        return categoryRepository.save(category);
    }
}
