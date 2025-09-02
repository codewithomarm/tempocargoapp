package com.tempocargo.app.tempo_cargo_api.auth.v1.revokedtoken.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "auth", name = "revoked_token",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "token_hash", name = "revoked_token_tokenHash_UNIQUE")
        })
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "RevokedToken's tokenHash should not be blank")
    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    @NotNull(message = "RevokedToken's revokedAt should not be blank")
    @Column(name = "revoked_at", nullable = false)
    private LocalDateTime revokedAt;

    @PrePersist
    public void prePersist() {
        initializerTimestamps();
    }

    public void initializerTimestamps() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (revokedAt == null) {
            revokedAt = now;
        }
    }
}
