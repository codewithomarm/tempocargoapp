package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordEmailVerificationRequest {

    @Email(message = "ForgotPasswordEmailVerificationRequest's email should be a valid email")
    private String email;
}
