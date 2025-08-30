package com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "client", name = "identity_type",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "code", name = "identity_type_code_UNIQUE"),
            @UniqueConstraint(columnNames = "display_name", name = "identity_type_displayName_UNIQUE")
        })
public class IdentityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "IdentityType's code should not be blank")
    @Column(name = "code", nullable = false, length = 4)
    private String code;

    @NotBlank(message = "IdentityType's displayName should not be blank")
    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;
}
