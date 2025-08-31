package com.tempocargo.app.tempo_cargo_api.shipping.v1.tempopackage.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import com.tempocargo.app.tempo_cargo_api.shipping.v1.packagestatus.model.PackageStatus;
import jakarta.persistence.*;
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
@Table(schema = "shipping", name = "tempo_package",
        indexes = {
            @Index(columnList = "client_id", name = "fk_tempo_package_client_idx"),
            @Index(columnList = "package_status_id", name = "fk_tempo_package_package_status_idx")
        })
public class TempoPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "TempoPackage's client should not be null")
    @JoinColumn(name = "client_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_tempo_package_client"))
    private Client client;

    @Column(name = "tracking_number", length = 30)
    private String trackingNumber;

    @ManyToOne
    @NotNull(message = "TempoPackage's packageStatus should not be null")
    @JoinColumn(name = "package_status_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_tempo_package_package_status"))
    private PackageStatus packageStatus;

    @NotNull(message = "TempoPackage's createdAt should not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "TempoPackage's updatedAt should not be null")
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
