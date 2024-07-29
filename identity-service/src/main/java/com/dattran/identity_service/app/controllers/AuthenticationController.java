package com.dattran.identity_service.app.controllers;

import com.dattran.identity_service.app.dtos.AuthenticationDTO;
import com.dattran.identity_service.app.dtos.IntrospectDTO;
import com.dattran.identity_service.app.responses.AuthenticationResponse;
import com.dattran.identity_service.app.responses.IntrospectResponse;
import com.dattran.identity_service.domain.services.AuthenticationService;
import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationDTO authenticationDTO, HttpServletRequest httpServletRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationDTO);
        return ApiResponse.<AuthenticationResponse>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(authenticationResponse)
                .status(HttpStatus.OK)
                .message("Login Successfully!")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest httpServletRequest) {
        authenticationService.logout(httpServletRequest);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Logout Successfully!")
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectDTO introspectDTO, HttpServletRequest httpServletRequest) {
        IntrospectResponse introspectResponse = authenticationService.introspect(introspectDTO);
        return ApiResponse.<IntrospectResponse>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .result(introspectResponse)
                .message("Token Valid!")
                .build();
    }
}
