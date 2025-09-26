package com.tempocargo.app.tempo_cargo_api.security.v1.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record RegisterResponse(
        ClientResponse client,
        IndividualResponse individual,
        BusinessResponse business,
        UserResponse user
) {
    public record ClientResponse(
           Long clientId,
           Integer poBoxNumber,
           String phoneNumberPrimary,
           String phoneNumberSecondary,
           String clientTypeCode,
           Long referredByClientId,
           Instant createdAt

    ){}

    public record IndividualResponse(
            String firstName,
            String lastName,
            String identityTypeCode,
            String identityNumber,
            LocalDate dateOfBirth,
            String nameCode,
            Instant createdAt
    ){}

    public record BusinessResponse(
            String name,
            String rucNumber,
            String contactName,
            String nameCode,
            Instant createdAt
    ){}

    public record UserResponse(
            Long userId,
            String username,
            String email,
            Instant createdAt
    ){}
}
