package com.tempocargo.app.tempo_cargo_api.auth.v1.session.model;

import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "auth", name = "session",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "access_token_hash", name = "session_accessTokenHash_UNIQUE"),
                @UniqueConstraint(columnNames = "refresh_token_hash", name = "session_refreshTokenHash_UNIQUE")
        },
        indexes = {
            @Index(columnList = "user_id", name = "fk_session_user_idx")
        })
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Session's user should not be null")
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_session_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TempoUser user;

    @NotBlank(message = "Session's accessTokenHash should not be null")
    @Column(name = "access_token_hash", nullable = false, unique = true, length = 64)
    private String accessTokenHash;

    @NotBlank(message = "Session's refreshTokenHash should not be null")
    @Column(name = "refresh_token_hash", nullable = false, unique = true, length = 64)
    private String refreshTokenHash;

    @NotNull(message = "Session's expiresAt should not be null")
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @NotNull(message = "Session's createdAt should not be null")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Session's lastActivityAt should not be null")
    @Column(name = "last_activity_at", nullable = false)
    private LocalDateTime lastActivityAt;

    @NotNull(message = "Session´s isActive should not be null")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 1024)
    private String userAgent;

    @PrePersist
    public void prePersist() {
        initializerTimestamps();
    }

    public void initializerTimestamps() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (createdAt == null) {
            createdAt = now;
        }

        if (lastActivityAt == null) {
            lastActivityAt = now;
        }
    }
}
