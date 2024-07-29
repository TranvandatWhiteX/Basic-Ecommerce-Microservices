package com.dattran.productservice.app.controllers;

import com.dattran.annotations.HasRoles;
import com.dattran.productservice.app.requests.UploadRequest;
import com.dattran.productservice.domain.entities.Category;
import com.dattran.productservice.domain.services.UploadService;
import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/uploads")
public class UploadController {
    UploadService uploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @HasRoles(roles = {"ADMIN"})
    public ApiResponse<List<String>> upload(@ModelAttribute @Valid UploadRequest uploadRequest, HttpServletRequest httpServletRequest) {
        List<String> links = uploadService.getLinksAfterUpload(uploadRequest);
        return ApiResponse.<List<String>>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(links)
                .status(HttpStatus.CREATED)
                .message("Category Created Successfully!")
                .build();
    }
}
