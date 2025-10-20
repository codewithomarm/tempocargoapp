package com.tempocargo.app.tempo_cargo_api.security.v1.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ForgotPasswordOtpVerificationResponse(
        String message,
        Instant requestExpiresAt
) {}
