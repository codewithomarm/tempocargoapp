package com.tempocargo.app.tempo_cargo_api.auth.v1.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateEmailRecuperationTokenRequest {

    @NotBlank(message = "UserUpdateEmailRecuperationTokenRequest's recuperationToken should not be blank")
    @Size(min = 10, max = 10, message = "UserUpdateEmaiLRecuperationTokenRequest's recuperationToken should be 10 digits")
    private String recuperationToken;
}
