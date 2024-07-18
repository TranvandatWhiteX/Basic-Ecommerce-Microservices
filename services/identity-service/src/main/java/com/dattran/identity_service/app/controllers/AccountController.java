package com.dattran.identity_service.app.controllers;

import com.dattran.identity_service.app.dtos.AccountDTO;
import com.dattran.identity_service.app.dtos.AuthenticationDTO;
import com.dattran.identity_service.app.dtos.VerifyDTO;
import com.dattran.identity_service.app.responses.AccountResponse;
import com.dattran.identity_service.app.responses.ApiResponse;
import com.dattran.identity_service.app.responses.AuthenticationResponse;
import com.dattran.identity_service.domain.services.AccountService;
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
    public ApiResponse<String> verifyAccount(@RequestBody VerifyDTO verifyDTO, HttpServletRequest httpServletRequest) {
        String message  = accountService.verifyAccount(verifyDTO);
        return ApiResponse.<String>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(message)
                .status(HttpStatus.OK)
                .message(message)
                .build();
    }
}
