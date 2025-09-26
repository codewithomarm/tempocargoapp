package com.tempocargo.app.tempo_cargo_api.auth.v1.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRegisterRequest {

    @NotNull(message = "UserRegisterRequest's clientId should not be null")
    private Long clientId;

    @NotBlank(message = "UserRegisterRequest's username should not be blank")
    @Size(min = 5, max = 30, message = "UserRegisterRequest's username should be between 5 min and 30 max characters")
    private String username;

    @NotBlank(message = "UserRegisterRequest's password should not be blank")
    @Pattern(
            regexp = "^(?!.*[._-]{2})[a-z0-9]+(?:[._-][a-z0-9]+)*$",
            message = "UserRegisterRequest's password can only be lowercase, digits and . _ -." +
                    "No double separator characters. Must start and end with alphanumeric."
    )
    private String password;

    @Email
    private String email;
}
