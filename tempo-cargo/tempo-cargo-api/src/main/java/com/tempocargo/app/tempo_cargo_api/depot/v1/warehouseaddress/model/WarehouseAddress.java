package com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseaddress.model;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehousetype.model.WarehouseType;
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
@Table(schema = "depot", name = "warehouse_address",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "warehouse_name", name = "warehouse_address_warehouseName_UNIQUE")
        },
        indexes = {
            @Index(columnList = "warehouse_type_id", name = "fk_warehouse_address_warehouse_type_idx")
        })
@Builder
public class WarehouseAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "WarehouseAddress's warehouseName should not be blank")
    @Column(name = "warehouse_name", nullable = false, length = 25)
    private String warehouseName;

    @ManyToOne
    @NotNull(message = "WarehouseAddress's warehouseType should not be null")
    @JoinColumn(name = "warehouse_type_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_warehouse_address_warehouse_type"))
    private WarehouseType warehouseType;

    @NotNull(message = "WarehouseAddress's isActive should not be null")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull(message = "WarehouseAddress's createdAt should not be null")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "WarehouseAddress's updatedAt should not be null")
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
