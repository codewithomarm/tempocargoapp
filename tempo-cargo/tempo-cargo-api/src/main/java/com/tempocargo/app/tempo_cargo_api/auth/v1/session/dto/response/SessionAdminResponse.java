package com.tempocargo.app.tempo_cargo_api.auth.v1.session.dto.response;

import java.time.Instant;

public record SessionAdminResponse(
        Long id,
        Long tempoUserId,
        String accessTokenHash,
        String refreshTokenHash,
        Instant expiresAt,
        Instant createdAt,
        Instant lastActivityAt,
        Boolean isActive,
        String ipAddress,
        String userAgent
) {}