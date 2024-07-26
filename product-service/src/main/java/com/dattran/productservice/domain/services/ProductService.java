package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.enums.UploadFolder;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.dtos.ProductDTO;
import com.dattran.productservice.app.dtos.ProductVariantDTO;
import com.dattran.productservice.app.requests.UploadRequest;
import com.dattran.productservice.domain.entities.Category;
import com.dattran.productservice.domain.entities.Product;
import com.dattran.productservice.domain.repositories.CategoryRepository;
import com.dattran.productservice.domain.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    UploadService uploadService;

    public Product createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new AppException(ResponseStatus.CATEGORY_NOT_FOUND));
        UploadRequest uploadRequest = UploadRequest.builder()
                .files(productDTO.getImages())
                .folder(UploadFolder.PRODUCT_IMAGES.getVal())
                .groupName(productDTO.getName())
                .build();
        List<String> links = uploadService.getLinksAfterUpload(uploadRequest);
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .category(category)
                .images(uploadService.getImagesFromLinks(links))
                .quantity(productDTO.getQuantity())
                .info(extractInfo(productDTO.getInfo()))
                .minPrice(productDTO.getPrice())
                .build();
        return productRepository.save(product);
    }

    public Product addVariantToProduct(List<ProductVariantDTO> productVariantDTOS) {
        return null;
    }

    private Map<String, String> extractInfo(String info) {
        Map<String, String> infoMap = new HashMap<>();
        String[] infoArray = info.split(",");
        for (String infoElement : infoArray) {
            StringTokenizer tokenizer = new StringTokenizer(infoElement, ":");
            infoMap.put(tokenizer.nextToken(), tokenizer.nextToken());
        }
        return infoMap;
    }
}
