package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank(message = "LoginRequest's username should not be blank")
    @Size(min = 5, max = 30, message = "LoginRequest's username should be between 5 min and 30 max characters")
    private String username;

    @NotBlank(message = "LoginRequest's password should not be blank")
    private String password;
}
