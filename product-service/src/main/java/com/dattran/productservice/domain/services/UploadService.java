package com.dattran.productservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import com.dattran.productservice.app.requests.UploadRequest;
import com.dattran.productservice.domain.repositories.UploadClient;
import com.dattran.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
}
