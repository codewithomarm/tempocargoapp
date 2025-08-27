package com.tempocargo.app.tempo_cargo_api.auth.v1.user.model;

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
@Table(schema = "auth", name = "tempo_user",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "username", name = "user_username_UNIQUE"),
            @UniqueConstraint(columnNames = "email", name = "user_email_UNIQUE")
        })
public class TempoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // client_id

    @NotBlank(message = "User's username should not be blank")
    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @NotBlank(message = "User's passwordHash should not be blank")
    @Column(name = "password_hash", nullable = false, length = 150)
    private String passwordHash;

    @NotBlank(message = "User´s email should not be blank")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull(message = "User's emailConfirmed should not be null")
    @Column(name = "email_confirmed", nullable = false)
    private Boolean emailConfirmed = false;

    @NotNull(message = "User's accountNonLocked should not be null")
    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked = false;

    @NotNull(message = "User's enabled should not be null")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "login_attempts")
    private int loginAttempts = 0;

    @Column(name = "recuperation_token", length = 10)
    private String recuperationToken;

    @Column(name = "recuperation_token_exp_at")
    private LocalDateTime recuperationTokenExpAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @NotNull(message = "User's createdAt should not be null")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "User's modifiedAt should not be null")
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

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

        if (modifiedAt == null) {
            modifiedAt = now;
        }
    }
}
