package com.tempocargo.app.tempo_cargo_api.security.v1.dto.response;

import java.time.Instant;

public record AuthResponse (
    String accessToken,
    String refreshToken,
    Instant accessTokenExpiresAt,
    Instant refreshTokenExpiresAt
) {}
