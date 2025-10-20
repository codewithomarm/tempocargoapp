package com.tempocargo.app.tempo_cargo_api.security.v1.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record RegisterAdminResponse(
        AdminClientResponse client,
        AdminUserResponse user,
        AdminRoleUserResponse roleUser
) {

    @Builder
    public record AdminClientResponse(
            Long clientId,
            Instant createdAt
    ){}

    @Builder
    public record AdminUserResponse(
            Long userId,
            String username,
            String email,
            Instant createdAt
    ){}

    @Builder
    public record AdminRoleUserResponse(
            Long roleUserId,
            Long roleId,
            String roleName,
            Long userId,
            String username
    ){}
}
