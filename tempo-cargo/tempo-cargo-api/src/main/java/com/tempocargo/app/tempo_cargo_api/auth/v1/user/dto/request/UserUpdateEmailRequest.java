package com.tempocargo.app.tempo_cargo_api.auth.v1.user.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateEmailRequest {

    @Email
    private String previousEmail;

    @Email
    private String newEmail;
}
