package com.tempocargo.app.tempo_cargo_api.client.v1.deliveryaddress.model;

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
@Table(schema = "client", name = "delivery_address",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"client_id", "address_name"}, name = "delivery_address_clientId_addressName_UNIQUE"),
        },
        indexes = {
            @Index(columnList = "client_id", name = "fk_delivery_address_client_idx")
        })
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "DeliveryAddress's client should not be null")
    @JoinColumn(name = "client_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_delivery_address_client"))
    private Client client;

    @NotBlank(message = "DeliveryAddress's addressName should not be blank")
    @Column(name = "address_name", nullable = false, length = 30)
    private String addressName;

    @NotNull(message = "DeliveryAddress's isDefault should not be null")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @NotBlank(message = "DeliveryAddress's address should not be blank")
    @Column(name = "address", nullable = false, length = 300)
    private String address;

    @NotBlank(message = "DeliveryAddress's corregimiento should not be blank")
    @Column(name = "corregimiento", nullable = false, length = 100)
    private String corregimiento;

    @NotBlank(message = "DeliveryAddress's district should not be blank")
    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @NotBlank(message = "DeliveryAddress's province should not blank")
    @Column(name = "province", nullable = false, length = 25)
    private String province;

    @NotBlank(message = "DeliveryAddress's country should not be blank")
    @Column(name = "country", nullable = false, length = 2)
    private String country = "PA";

    @NotNull(message = "DeliveryAddress's createdAt should not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "DeliveryAddress's updatedAt should not be null")
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
