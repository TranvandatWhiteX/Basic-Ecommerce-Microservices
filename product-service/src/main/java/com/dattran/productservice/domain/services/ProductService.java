package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.dtos.ProductDTO;
import com.dattran.productservice.domain.entities.Category;
import com.dattran.productservice.domain.entities.Product;
import com.dattran.productservice.domain.entities.ProductVariant;
import com.dattran.productservice.domain.repositories.CategoryRepository;
import com.dattran.productservice.domain.repositories.ProductRepository;
import com.dattran.productservice.domain.repositories.ProductVariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductVariantRepository productVariantRepository;

    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new AppException(ResponseStatus.CATEGORY_NOT_FOUND));
        // Add product variant to product
        List<ProductVariant> productVariants = addProductVariants(productDTO);
        BigDecimal maxPrice = productVariants.stream().map(ProductVariant::getPrice)
                .reduce(BigDecimal::max).orElse(productDTO.getPrice());
        Long qty = productVariants.stream().map(ProductVariant::getQuantity)
                .reduce(Long::sum).orElse(productDTO.getQuantity());
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .category(category)
                .images(productDTO.getImages())
                .quantity(qty)
                .info(productDTO.getInfo())
                .minPrice(productDTO.getPrice())
                .productVariants(productVariants)
                .maxPrice(maxPrice)
                .build();
        return productRepository.save(product);
    }

    private List<ProductVariant> addProductVariants(ProductDTO productDTO) {
        if (productDTO.getProductVariants().isEmpty()) {
            List<ProductVariant> productVariants = List.of(ProductVariant.builder()
                            .price(productDTO.getPrice())
                            .isDeleted(false)
                            .quantity(productDTO.getQuantity())
                    .build());
            return productVariantRepository.saveAll(productVariants);
        } else {
//            Todo: Generate all combinations for attribute pairs (Tạo tất cả tổ hợp cho các cặp thuộc tính)
            List<ProductVariant> productVariants = productDTO.getProductVariants().stream().map(productVariantDTO -> {
                ProductVariant productVariant = new ProductVariant();
                productVariant.setAttributes(productVariantDTO.getAttributes());
                productVariant.setIsDeleted(false);
                productVariant.setQuantity(productVariantDTO.getQuantity());
                productVariant.setPrice(productVariantDTO.getPrice());
                return productVariant;
            }).toList();
            return productVariantRepository.saveAll(productVariants);
        }
    }
}
