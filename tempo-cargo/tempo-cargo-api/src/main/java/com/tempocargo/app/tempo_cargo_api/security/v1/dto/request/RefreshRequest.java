package com.tempocargo.app.tempo_cargo_api.security.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshRequest {

    @NotBlank(message = "RefreshRequest's refreshToken should not be blank")
    private String refreshToken;
}
