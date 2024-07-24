package com.dattran.uploadservice.app.controllers;

import com.dattran.annotations.HasRoles;
import com.dattran.responses.ApiResponse;
import com.dattran.uploadservice.app.requests.UploadRequest;
import com.dattran.uploadservice.domain.services.AwsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/uploads")
public class UploadController {
    AwsService awsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> upload(@ModelAttribute UploadRequest uploadRequest,
                                            HttpServletRequest httpServletRequest) {
        List<String> links = awsService.uploadFiles(uploadRequest.getFiles(), uploadRequest.getFolder());
        return ApiResponse.<List<String>>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(links)
                .status(HttpStatus.CREATED)
                .message("Upload Successfully!")
                .build();
    }
}
