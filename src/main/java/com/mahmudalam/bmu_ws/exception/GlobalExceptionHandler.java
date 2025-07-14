package com.mahmudalam.bmu_ws.exception;

import com.mahmudalam.bmu_ws.dto.response.UserResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle missing request params like @RequestParam
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public static ResponseEntity<UserResponse<Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        String error = "Missing required request parameter: " + ex.getParameterName();
        return ResponseEntity.badRequest().body(new UserResponse<>(false, null, error));
    }

    // Handle @Valid or @Validated errors in @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new UserResponse<>(false, null, error));
    }

    // Handle other constraint violations (like @RequestParam @NotNull)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<UserResponse<Object>> handleConstraintViolations(ConstraintViolationException ex) {
        String error = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new UserResponse<>(false, null, error));
    }

    // Generic fallback for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserResponse<Object>> handleOtherErrors(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UserResponse<>(false, null, ex.getMessage()));
    }
}
