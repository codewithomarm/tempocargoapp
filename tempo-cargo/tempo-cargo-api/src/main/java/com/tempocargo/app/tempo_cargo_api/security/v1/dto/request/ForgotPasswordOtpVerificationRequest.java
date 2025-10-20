package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordOtpVerificationRequest {

    @Email(message = "ForgotPasswordOtpVerificationRequest's email should be a valid email")
    private String email;

    @Pattern(regexp = "\\d{6}", message = "ForgotPasswordOtpVerificationRequest's otp should be 6 numeric digits")
    private String otp;
}
