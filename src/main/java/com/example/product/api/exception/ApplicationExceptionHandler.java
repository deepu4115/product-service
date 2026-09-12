package com.example.product.api.exception;

import com.example.product.domain.exception.InsufficientStockException;
import com.example.product.domain.exception.ProductNotFoundByIdException;
import com.example.product.shared.ErrorStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private <T> ResponseEntity<ErrorStructure<T>> handleException(HttpStatus status, String message, T rootCause) {
        return ResponseEntity
                .status(status)
                .body(ErrorStructure.<T>builder()
                        .status(status.value())
                        .message(message)
                        .rootCause(rootCause)
                        .build());
    }

    @ExceptionHandler(ProductNotFoundByIdException.class)
    public ResponseEntity<ErrorStructure<String>> handleProductNotFoundByIdException(ProductNotFoundByIdException ex) {
        return handleException(HttpStatus.NOT_FOUND, ex.getMessage(), "Product not found by the given Id");
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorStructure<String>> handleInsufficientStockException(InsufficientStockException ex) {
        return handleException(HttpStatus.BAD_REQUEST, ex.getMessage(), "Insufficient stock for requested quantity");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorStructure<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return handleException(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }
}
