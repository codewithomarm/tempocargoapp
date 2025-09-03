package com.tempocargo.app.tempo_cargo_api.depot.v1.warehousecn.model;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseaddress.model.WarehouseAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(schema = "depot", name = "warehouse_cn",
        indexes = {
                @Index(columnList = "warehouse_address_id", name = "fk_warehouse_cn_warehouse_address_idx")
        })
public class WarehouseCN {

    @Id
    @Column(name = "warehouse_address_id")
    private Long id;

    @OneToOne
    @MapsId
    @NotNull(message = "WarehouseCN's warehouseAddress should not be null")
    @JoinColumn(name = "warehouse_address_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_warehouse_cn_warehouse_address"))
    private WarehouseAddress warehouseAddress;

    @NotBlank(message = "WarehouseCN's fullAddressCN should not be blank")
    @Column(name = "full_address_cn", nullable = false, columnDefinition = "TEXT")
    private String fullAddressCN;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
}
