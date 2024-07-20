package com.dattran.notification_service.app.controllers;

import com.dattran.notification_service.app.requests.EmailRequest;
import com.dattran.notification_service.app.responses.ApiResponse;
import com.dattran.notification_service.domain.enums.ResponseStatus;
import com.dattran.notification_service.domain.exceptions.AppException;
import com.dattran.notification_service.domain.services.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/notifications")
public class EmailController {
    EmailService emailService;

    @PostMapping("/email")
    public ApiResponse<Boolean> sendEmail(@RequestBody EmailRequest emailRequest, HttpServletRequest httpServletRequest) {
        CompletableFuture<Boolean> future = emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getTemplate(), emailRequest.getVariables());
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .build();
        try {
            boolean isSendingEmail = future.get();
            if (isSendingEmail) {
                response.setStatus(HttpStatus.OK);
                response.setMessage("Sending Email Successfully");
                response.setResult(true);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Sending Email Failed");
                response.setResult(false);
            }
            return response;
        }  catch (ExecutionException | InterruptedException e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(e.getMessage());
            response.setResult(false);
        }
        return response;
    }
}
