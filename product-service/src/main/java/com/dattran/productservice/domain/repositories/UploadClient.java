package com.dattran.productservice.domain.repositories;

import com.dattran.productservice.app.requests.UploadRequest;
import com.dattran.responses.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "upload-service", url = "${services.upload}")
public interface UploadClient {
    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<List<String>> upload(@ModelAttribute UploadRequest uploadRequest);
}
