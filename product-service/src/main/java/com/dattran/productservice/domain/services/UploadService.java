package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.requests.UploadRequest;
import com.dattran.productservice.domain.entities.Image;
import com.dattran.productservice.domain.repositories.UploadClient;
import com.dattran.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadService {
    UploadClient uploadClient;

    public List<String> getLinksAfterUpload(UploadRequest uploadRequest) {
        ApiResponse<List<String>> apiResponse = uploadClient.upload(uploadRequest);
        if (apiResponse.getResult().isEmpty()) {
            throw new AppException(ResponseStatus.CANNOT_GET_LINKS);
        }
        return apiResponse.getResult();
    }

    public List<Image> getImagesFromLinks(List<String> links) {
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
