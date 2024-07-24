package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.enums.UploadFolder;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.dtos.BrandDTO;
import com.dattran.productservice.app.requests.UploadRequest;
import com.dattran.productservice.domain.entities.Brand;
import com.dattran.productservice.domain.entities.Image;
import com.dattran.productservice.domain.repositories.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;
    UploadService uploadService;

    public Brand createBrand(BrandDTO brandDTO) {
        if (brandRepository.existsByName(brandDTO.getName())) {
            throw new AppException(ResponseStatus.BRAND_ALREADY_EXIST);
        }
        UploadRequest uploadRequest = UploadRequest.builder()
                .files(brandDTO.getFiles())
                .folder(UploadFolder.BRAND_IMAGES.getVal())
                .build();
        List<String> links = uploadService.getLinksAfterUpload(uploadRequest);
        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .isDeleted(false)
                .images(getImagesFromLinks(links))
                .build();
        return brandRepository.save(brand);
    }

    private List<Image> getImagesFromLinks(List<String> links) {
        List<Image> images = new ArrayList<>();
        for (int i=0; i<links.size(); i++) {
            Image image = new Image();
            image.setUrl(links.get(i));
            image.setIsCover(i == 0);
            images.add(image);
        }
        return images;
    }
}
