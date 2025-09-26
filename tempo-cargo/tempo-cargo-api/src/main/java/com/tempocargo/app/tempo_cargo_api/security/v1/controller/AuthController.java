package com.tempocargo.app.tempo_cargo_api.security.v1.controller;

import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.LoginRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.AuthResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        AuthResponse response = authenticationService.login(request, httpServletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(
            @RequestBody Map<String, String> body, HttpServletRequest httpServletRequest) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        return authenticationService.refresh(refreshToken, httpServletRequest);
    }
}
