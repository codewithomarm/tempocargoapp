package com.tempocargo.app.tempo_cargo_api.security.v1.exception.handler;

import com.tempocargo.app.tempo_cargo_api.common.v1.dto.response.ApiErrorResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class SecurityExceptionHandler {

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

    @ExceptionHandler(UnregisteredEmailException.class)
    public ResponseEntity<ApiErrorResponse> unregisteredEmailExceptionHandler(UnregisteredEmailException ex,
                                                                              HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Unregistered Email")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ForgotPasswordOtpNotRequestedException.class)
    public ResponseEntity<ApiErrorResponse> forgotPasswordOtpNotRequestedExceptionHandler(ForgotPasswordOtpNotRequestedException ex,
                                                                                          HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Forgot Password Recovery OTP Not Requested")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ForgotPasswordOtpExpiredException.class)
    public ResponseEntity<ApiErrorResponse> forgotPasswordOtpExpiredExceptionHandler(ForgotPasswordOtpExpiredException ex,
                                                                                     HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Forgot Password Verificiation Token Expired")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ForgotPasswordNotRequestedException.class)
    public ResponseEntity<ApiErrorResponse> forgotPasswordNotRequestedExceptionHandler(ForgotPasswordNotRequestedException ex,
                                                                                       HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Forgot Password Process Not Requested")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ForgotPasswordRequestExpired.class)
    public ResponseEntity<ApiErrorResponse> forgotPasswordRequestExpiredExceptionHandler(ForgotPasswordRequestExpired ex,
                                                                                         HttpServletRequest request) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .title("Forgot Password Process Request Expired")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(List.of())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
