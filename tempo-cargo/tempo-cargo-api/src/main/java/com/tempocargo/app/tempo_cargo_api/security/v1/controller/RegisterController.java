package com.tempocargo.app.tempo_cargo_api.security.v1.controller;

import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.EmailVerificationRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.OtpVerificationRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.RegisterRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.EmailVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.OtpVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.RegisterResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/auth/registration")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/email")
    public ResponseEntity<EmailVerificationResponse> emailVerification(
            @Valid @RequestBody EmailVerificationRequest request) throws IOException {
        EmailVerificationResponse response = registerService.emailVerification(request);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @PostMapping("/otp")
    public ResponseEntity<OtpVerificationResponse> otpVerification(@Valid @RequestBody OtpVerificationRequest request) {
        OtpVerificationResponse response = registerService.otpVerification(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request,
                                                     HttpServletRequest servlet) {
        RegisterResponse response = registerService.register(request, servlet);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
