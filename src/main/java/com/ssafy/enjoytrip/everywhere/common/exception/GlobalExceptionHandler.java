package com.ssafy.enjoytrip.everywhere.common.exception;

import com.ssafy.enjoytrip.everywhere.common.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidToken(ApiException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().status())
                .body(ApiResponse.error(ex.getErrorCode(), ex.getErrorCode().message()));
    }
}