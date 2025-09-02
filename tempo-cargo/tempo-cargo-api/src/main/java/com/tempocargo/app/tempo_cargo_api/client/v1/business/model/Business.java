package com.tempocargo.app.tempo_cargo_api.client.v1.business.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
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
@Table(schema = "client", name = "business",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "name", name = "business_name_UNIQUE"),
            @UniqueConstraint(columnNames = "ruc_number", name = "business_rucNumber_UNIQUE"),
            @UniqueConstraint(columnNames = "name_code", name = "business_nameCode_UNIQUE")
        },
        indexes = {
            @Index(columnList = "client_id", name = "fk_business_client_idx")
        })
public class Business {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @NotNull(message = "Business's client_id should not be null")
    @JoinColumn(name = "client_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_business_client"))
    private Client client;

    @NotBlank(message = "Business's name should not be blank")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Business's rucNumber should not be blank")
    @Column(name = "ruc_number", nullable = false, length = 50)
    private String rucNumber;

    @NotBlank(message = "Business's contactName should not be blank")
    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @NotBlank(message = "Business's nameCode should not be blank")
    @Column(name = "name_code", nullable = false, length = 18)
    private String nameCode;

    @NotNull(message = "Business's createdAt should not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Business's updatedAt should not be null")
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
