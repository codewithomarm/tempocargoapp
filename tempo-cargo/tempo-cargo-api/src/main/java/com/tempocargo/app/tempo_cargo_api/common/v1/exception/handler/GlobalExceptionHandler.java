package com.tempocargo.app.tempo_cargo_api.common.v1.exception.handler;

import com.tempocargo.app.tempo_cargo_api.common.v1.dto.response.ApiErrorResponse;
import com.tempocargo.app.tempo_cargo_api.common.v1.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationExceptionHandler(MethodArgumentNotValidException ex,
                                                                          HttpServletRequest request) {
        List<ApiErrorResponse.FieldErrorDetail> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ApiErrorResponse.FieldErrorDetail(
                        err.getField(), err.getDefaultMessage()))
                .toList();

        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Validation Error")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("One or more fields failed validation.")
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException ex,
                                                                            HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Illegal Argument")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex,
                                                                             HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException ex,
                                                                         HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Resource Already Exists")
                .status(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<ApiErrorResponse> invalidVerificationTokenExceptionHandler(InvalidVerificationTokenException ex,
                                                                                     HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Invalid Verification Token")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<ApiErrorResponse> invalidUsernameExceptionHandler(InvalidUsernameException ex,
                                                                            HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Invalid Username")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<ApiErrorResponse> weakPasswordExceptionHandler(WeakPasswordException ex,
                                                                         HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Weak Password")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
