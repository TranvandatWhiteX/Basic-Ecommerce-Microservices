package com.dattran.exceptions;

import com.dattran.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({AppException.class})
    ApiResponse<Void> handlingAppException(AppException exception, HttpServletRequest httpServletRequest) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    ApiResponse<Void> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();
    }
}
