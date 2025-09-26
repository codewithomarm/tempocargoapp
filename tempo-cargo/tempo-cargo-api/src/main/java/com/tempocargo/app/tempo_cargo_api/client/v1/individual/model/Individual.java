package com.tempocargo.app.tempo_cargo_api.client.v1.individual.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.model.IdentityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "client", name = "individual",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "identity_number", name = "individual_identityNumber_UNIQUE"),
            @UniqueConstraint(columnNames = "name_code", name = "individual_nameCode_UNIQUE")
        },
        indexes = {
            @Index(columnList = "client_id", name = "fk_individual_client_idx"),
            @Index(columnList = "identity_type_id", name = "fk_individual_identity_type_idx")
        })
@Builder
public class Individual {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @NotNull(message = "Individual's client_id should not be null")
    @JoinColumn(name = "client_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_individual_client"))
    private Client client;

    @NotBlank(message = "Individual's firstName should not be blank")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Individual's lastName should not be blank")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @NotNull(message = "Individual's identityType should not be null")
    @JoinColumn(name = "identity_type_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_individual_identity_type"))
    private IdentityType identityType;

    @NotBlank(message = "Individual's identityNumber should no be blank")
    @Column(name = "identity_number", nullable = false, length = 20)
    private String identityNumber;

    @NotNull(message = "Individual's dateOfBirth should not be null")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Individual's nameCode should not be blank")
    @Column(name = "name_code", nullable = false, length = 18)
    private String nameCode;

    @NotNull(message = "Individual's createdAt should not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Individual's updatedAt should not be null")
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
