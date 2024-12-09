package com.plantify.transaction.global.exception;

import com.plantify.transaction.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(ApplicationException e) {
        HttpStatus status = e.getHttpStatus();
        log.error(e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.fail(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        log.error("{}  {}", request.getRemoteHost(), request.getRequestURI());
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러");
    }
}