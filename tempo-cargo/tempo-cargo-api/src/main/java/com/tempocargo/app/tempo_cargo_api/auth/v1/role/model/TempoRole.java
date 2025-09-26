package com.tempocargo.app.tempo_cargo_api.auth.v1.role.model;

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
@Table(schema = "auth", name = "tempo_role",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "name", name = "role_name_UNIQUE")
        })
@Builder
public class TempoRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role's name should not be blank")
    @Column(name = "name", nullable = false, length = 50)
    private String name;
}
