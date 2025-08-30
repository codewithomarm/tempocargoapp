package com.tempocargo.app.tempo_cargo_api.client.v1.client.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.model.ClientType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(schema = "client", name = "client",
        indexes = {
            @Index(columnList = "type_id", name = "fk_client_client_type_idx"),
            @Index(columnList = "referred_by_client_id", name = "fk_client_client_idx")
        })
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Client's phoneNumberPrimary should not be blank")
    @Column(name = "phone_number_pri", nullable = false, length = 20)
    private String phoneNumberPrimary;

    @Column(name = "phone_number_sec", length = 20)
    private String phoneNumberSecondary;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_client_client_type"))
    private ClientType type;

    @Column(name = "terms_accepted_at")
    private LocalDateTime termsAcceptedAt;

    @Column(name = "terms_version", length = 5)
    private String termsVersion;

    @ManyToOne
    @JoinColumn(name = "referred_by_client_id", foreignKey = @ForeignKey(name = "fk_client_client"))
    private Client referredBy;

    @NotNull(message = "Client's createdAt should not be null")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Client's updatedAt should not be null")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        initializerTimestamps();
    }

    public void initializerTimestamps() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }
}
