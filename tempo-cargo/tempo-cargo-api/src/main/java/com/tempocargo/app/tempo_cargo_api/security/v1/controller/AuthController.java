package com.tempocargo.app.tempo_cargo_api.security.v1.controller;

import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.LoginRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.RefreshRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.RegisterRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.AuthResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.RegisterResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.service.AuthenticationService;
import com.tempocargo.app.tempo_cargo_api.security.v1.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = registerService.register(request);
        return ResponseEntity.ok(response);
    }

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
}