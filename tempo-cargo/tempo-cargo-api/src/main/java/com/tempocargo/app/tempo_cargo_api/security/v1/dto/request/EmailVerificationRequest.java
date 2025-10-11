package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerificationRequest {

    @NotBlank(message = "EmailVerificationRequest's email should not be blank")
    @Email(message = "EmailVerificationRequest's should be a valid email")
    private String email;
}
