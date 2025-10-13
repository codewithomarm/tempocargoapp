package com.tempocargo.app.tempo_cargo_api.auth.v1.user.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "auth", name = "tempo_user",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "client_id", name = "user_clientId_UNIQUE"),
            @UniqueConstraint(columnNames = "username", name = "user_username_UNIQUE"),
            @UniqueConstraint(columnNames = "email", name = "user_email_UNIQUE")
        },
        indexes = {
            @Index(columnList = "client_id", name = "fk_user_client_idx")
        })
@Builder
public class TempoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull(message = "User's client should not be null")
    @JoinColumn(name = "client_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_tempo_user_client"))
    private Client client;

    @NotBlank(message = "User's username should not be blank")
    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @NotBlank(message = "User's passwordHash should not be blank")
    @Column(name = "password_hash", nullable = false, length = 150)
    private String passwordHash;

    @NotBlank(message = "User´s email should not be blank")
    @Column(name = "email", nullable = false)
    private String email;

    @Builder.Default
    @NotNull(message = "User's emailConfirmed should not be null")
    @Column(name = "email_confirmed", nullable = false)
    private Boolean emailConfirmed = false;

    @Builder.Default
    @NotNull(message = "User's accountNonLocked should not be null")
    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked = true;

    @Builder.Default
    @NotNull(message = "User's enabled should not be null")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Builder.Default
    @Column(name = "login_attempts")
    private int loginAttempts = 0;

    @Column(name = "recuperation_token", length = 6)
    private String recuperationToken;

    @Column(name = "recuperation_token_exp_at")
    private LocalDateTime recuperationTokenExpAt;

    @Builder.Default
    @Column(name = "is_recuperation_token_verified")
    private Boolean isRecuperationTokenVerified = false;

    @Column(name = "reset_password_request_exp_at")
    private LocalDateTime resetPasswordRequestExpAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @NotNull(message = "User's createdAt should not be null")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "User's updatedAt should not be null")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        initializerTimestamps();
    }

    public void initializerTimestamps() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (lastLoginAt == null) {
            lastLoginAt = now;
        }

        if (lastActivityAt == null) {
            lastActivityAt = now;
        }

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }
}
