package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterAdminRequest {

    @NotBlank(message = "RegisterAdminRequest's username should not be blank")
    @Size(min = 5, max = 30, message = "RegisterAdminRequest's username should be 5 chars min and 30 chars max")
    private String username;

    @NotBlank(message = "RegisterAdminRequest's password should not be blank")
    private String password;

    @Email(message = "RegisterAdminRequest's email should be a valid email")
    private String email;
}
