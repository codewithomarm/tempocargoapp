package com.tempocargo.app.tempo_cargo_api.security.v1.controller;

import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.*;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.AuthResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.ForgotPasswordEmailVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.ForgotPasswordOtpVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.ResetPasswordResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.service.AuthenticationService;
import com.tempocargo.app.tempo_cargo_api.security.v1.service.ForgotPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        AuthResponse response = authenticationService.login(request, httpServletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request, HttpServletRequest httpServletRequest) {
        return authenticationService.refresh(request.getRefreshToken(), httpServletRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        authenticationService.logout(request);
        return ResponseEntity.ok("Logout Successfully");
    }

    @PostMapping("/forgot-password/email")
    public ResponseEntity<ForgotPasswordEmailVerificationResponse> forgotPasswordEmailVerification(
            @Valid @RequestBody ForgotPasswordEmailVerificationRequest request) throws IOException {
        String message = forgotPasswordService.emailVerification(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ForgotPasswordEmailVerificationResponse(message));
    }

    @PostMapping("/forgot-password/otp")
    public ResponseEntity<ForgotPasswordOtpVerificationResponse> forgotPasswordOtpVerification(
            @Valid @RequestBody ForgotPasswordOtpVerificationRequest request) {
        ForgotPasswordOtpVerificationResponse response = forgotPasswordService.otpVerification(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ResetPasswordResponse response = forgotPasswordService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}