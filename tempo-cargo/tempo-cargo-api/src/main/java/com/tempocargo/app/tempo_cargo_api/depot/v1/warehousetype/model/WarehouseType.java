package com.tempocargo.app.tempo_cargo_api.depot.v1.warehousetype.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "depot", name = "warehouse_type",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "code", name = "warehouse_type_code_UNIQUE"),
            @UniqueConstraint(columnNames = "display_name", name = "warehouse_type_displayName_UNIQUE")
        })
public class WarehouseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "DepotType's code should not be blank")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @NotBlank(message = "DepotType's displayName should not be blank")
    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;
}
