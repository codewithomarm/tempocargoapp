package com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseus.model;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseaddress.model.WarehouseAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(schema = "depot", name = "warehouse_us",
        indexes = {
            @Index(columnList = "warehouse_address_id", name = "fk_warehouse_us_warehouse_address_idx")
        })
public class WarehouseUS {

    @Id
    @Column(name = "warehouse_address_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @NotNull(message = "WarehouseUS's warehouseAddress should not be null")
    @JoinColumn(name = "warehouse_address_id", nullable = false, referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_warehouse_us_warehouse_address"))
    private WarehouseAddress warehouseAddress;

    @NotBlank(message = "WarehouseUS's streetLine1 should not be blank")
    @Column(name = "street_line_1", nullable = false, length = 200)
    private String streetLine1;

    @Column(name = "street_line_2", length = 100)
    private String streetLine2;

    @NotBlank(message = "WarehouseUS's city should not be blank")
    @Column(name = "city", nullable = false, length = 85)
    private String city;

    @NotBlank(message = "WarehouseUS's stateProvince should not be blank")
    @Column(name = "state_province", nullable = false, length = 50)
    private String stateProvince;

    @NotBlank(message = "WarehouseUS's postalCode should not be blank")
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @NotBlank(message = "WarehouseUS's countryCode should not be blank")
    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode = "US";

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;
}
