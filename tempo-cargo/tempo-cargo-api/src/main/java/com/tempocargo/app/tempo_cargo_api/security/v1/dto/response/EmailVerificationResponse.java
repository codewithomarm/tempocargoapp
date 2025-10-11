package com.tempocargo.app.tempo_cargo_api.security.v1.dto.response;

public record EmailVerificationResponse(
    String message,
    String emailVerificationToken,
    long expiresIn
) {
}
