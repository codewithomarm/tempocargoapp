package com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseco.model;

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
@Table(schema = "depot", name = "warehouse_co",
        indexes = {
                @Index(columnList = "warehouse_address_id", name = "fk_warehouse_co_warehouse_address_idx")
        })
public class WarehouseCO {

    @Id
    @Column(name = "warehouse_address_id")
    private Long id;

    @OneToOne
    @MapsId
    @NotNull(message = "WarehouseCN's warehouseAddress should not be null")
    @JoinColumn(name = "warehouse_address_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_warehouse_cn_warehouse_address"))
    private WarehouseAddress warehouseAddress;

    @NotBlank(message = "WarehouseCO's streetLine should not be blank")
    @Column(name = "street_line", nullable = false, length = 300)
    private String streetLine;

    @NotBlank(message = "WarehouseCO's barrio should not be blank")
    @Column(name = "barrio", nullable = false, length = 100)
    private String barrio;

    @NotBlank(message = "WarehouseCO's city should not be blank")
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @NotBlank(message = "WarehouseCO's department should not be blank")
    @Column(name = "department", nullable = false, length = 30)
    private String department;

    @NotBlank(message = "WarehouseCO's postalCode should not be blank")
    @Column(name = "postal_code", nullable = false, length = 6)
    private String postalCode;

    @NotBlank(message = "WarehouseCO's countryCode should not be blank")
    @Column(name = "country_code", nullable = false, length = 2)
    @Builder.Default
    private String countryCode = "CO";

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
}
