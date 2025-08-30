package com.tempocargo.app.tempo_cargo_api.client.v1.clientaddress.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.addresstype.model.AddressType;
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
@Table(schema = "client", name = "client_address",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"client_id", "address_name"}, name = "client_address_clientId_addressName_UNIQUE")
        },
        indexes = {
            @Index(columnList = "address_type_id", name = "fk_client_address_address_type_idx")
        })
public class ClientAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "ClientAddress's client should not be null")
    @JoinColumn(name = "client_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_client_address_client"))
    private Client client;

    @NotBlank(message = "ClientAddress's addressName should not be blank")
    @Column(name = "address_name", nullable = false, length = 100)
    private String addressName;

    @ManyToOne
    @NotNull(message = "ClientAddress's addressType should not be null")
    @JoinColumn(name = "address_type_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_client_address_address_type"))
    private AddressType addressType;

    @NotNull(message = "ClientAddress's isDefault should not be null")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @NotNull(message = "ClientAddress's createdAt should not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "ClientAddress's updatedAt should not be null")
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
