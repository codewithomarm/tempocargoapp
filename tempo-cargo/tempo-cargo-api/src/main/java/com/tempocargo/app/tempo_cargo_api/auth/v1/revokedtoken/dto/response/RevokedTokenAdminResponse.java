package com.tempocargo.app.tempo_cargo_api.auth.v1.revokedtoken.dto.response;

import java.time.Instant;

public record RevokedTokenAdminResponse(
        Long id,
        String tokenHash,
        Instant revokedAt
) {}
