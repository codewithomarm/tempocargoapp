package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpVerificationRequest {

    @Email
    private String email;

    @Pattern(regexp = "\\d{6}", message = "OtpVerificationRequest's otp should be 6 numeric digits")
    private String otp;

    @NotBlank(message = "OtpVerificationRequest's otp should not be blank")
    private String emailVerificationToken;
}
