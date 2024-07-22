package com.dattran.identity_service.app.controllers;

import com.dattran.identity_service.app.dtos.AccountDTO;
import com.dattran.identity_service.app.dtos.ChangePasswordDTO;
import com.dattran.identity_service.app.dtos.ForgotPasswordDTO;
import com.dattran.identity_service.app.dtos.VerifyDTO;
import com.dattran.identity_service.app.responses.AccountResponse;
import com.dattran.identity_service.app.responses.VerifyResponse;
import com.dattran.identity_service.domain.services.AccountService;
import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/accounts")
@Slf4j
public class AccountController {
    AccountService accountService;

    @PostMapping
    public ApiResponse<AccountResponse> createAccount(@RequestBody AccountDTO accountDTO, HttpServletRequest httpServletRequest) {
        AccountResponse accountResponse = accountService.createAccount(accountDTO);
        return ApiResponse.<AccountResponse>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(accountResponse)
                .status(HttpStatus.CREATED)
                .message("Create Account Successfully!")
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verifyAccount(@RequestBody VerifyDTO verifyDTO, HttpServletRequest httpServletRequest) {
        VerifyResponse verified = accountService.verifyAccount(verifyDTO);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(verified.isVerified() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(verified.getMessage())
                .build();
    }

    @PostMapping("/verify-pass")
    public ApiResponse<Void> verifyPass(@RequestBody VerifyDTO verifyDTO, HttpServletRequest httpServletRequest) {
        VerifyResponse verified = accountService.verifyChangePassword(verifyDTO);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(verified.isVerified() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(verified.getMessage())
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO, HttpServletRequest httpServletRequest) {
        accountService.forgotPassword(forgotPasswordDTO);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Verify password with otp in your email")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, HttpServletRequest httpServletRequest, @PathVariable String id) {
        accountService.changePassword(id, changePasswordDTO);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Verify password with otp in your email")
                .build();
    }
}
