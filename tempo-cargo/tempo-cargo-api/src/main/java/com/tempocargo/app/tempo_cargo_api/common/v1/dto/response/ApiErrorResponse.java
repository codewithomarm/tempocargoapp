package com.tempocargo.app.tempo_cargo_api.common.v1.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ApiErrorResponse(
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp,
        List<FieldErrorDetail> errors
) {
    public static record FieldErrorDetail(
            String field,
            String message
    ){}
}
