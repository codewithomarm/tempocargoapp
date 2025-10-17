package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @Email(message = "ResetPasswordRequest's email should be a valid email")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#.])[A-Za-z\\d@$!%*?&#.]{8,64}$",
            message = "ResetPasswordRequest's newPassword must contain upper, lower, digit, and special character"
    )
    private String newPassword;
}
