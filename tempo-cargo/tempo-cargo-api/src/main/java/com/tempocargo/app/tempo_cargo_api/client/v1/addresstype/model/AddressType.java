package com.tempocargo.app.tempo_cargo_api.client.v1.addresstype.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "client", name = "address_type",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "code", name = "address_type_code_UNIQUE"),
            @UniqueConstraint(columnNames = "display_name", name = "address_type_displayName_UNIQUE")
        })
public class AddressType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "AddressType's code should not be blank")
    @Column(name = "code", nullable = false, length = 4)
    private String code;

    @NotBlank(message = "AddressType's displayName should not be blank")
    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;
}
