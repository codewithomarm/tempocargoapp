package com.tempocargo.app.tempo_cargo_api.shipping.v1.packagestatus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "shipping", name = "package_status",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "code", name = "package_status_code_UNIQUE"),
            @UniqueConstraint(columnNames = "display_name", name = "package_status_displayName_UNIQUE")
        })
public class PackageStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "PackageStatus's code should not be blank")
    @Column(name = "code", nullable = false, length = 4)
    private String code;

    @NotBlank(message = "PackageStatus's displayName should not be blank")
    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;
}
