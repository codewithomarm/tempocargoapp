package com.tempocargo.app.tempo_cargo_api.auth.v1.user.dto.response;

import java.time.Instant;

public record UserAdminResponse(
        Long id,
        Long client_id,
        String username,
        String email,
        Boolean emailConfirmed,
        Boolean accountNonLocked,
        Boolean enabled,
        int loginAttempts,
        Instant lastLoginAt,
        Instant lastActivityAt,
        Instant createdAt,
        Instant updatedAt
) {}
