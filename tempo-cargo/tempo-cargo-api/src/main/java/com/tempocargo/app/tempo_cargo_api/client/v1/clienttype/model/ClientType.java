package com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "client", name = "client_type",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "code", name = "client_type_code_UNIQUE"),
            @UniqueConstraint(columnNames = "display_name", name = "client_type_displayName_UNIQUE")
        })
@Builder
public class ClientType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ClientType's code should not be blank")
    @Column(name = "code", nullable = false, length = 4)
    private String code;

    @NotBlank(message = "ClientType's displayName should not be blank")
    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;
}
