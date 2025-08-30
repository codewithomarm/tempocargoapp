package com.tempocargo.app.tempo_cargo_api.client.v1.ptyaddress.model;

import com.tempocargo.app.tempo_cargo_api.client.v1.clientaddress.model.ClientAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "client", name = "pty_address",
        indexes = {
            @Index(columnList = "client_address_id", name = "fk_pty_address_client_address_idx")
        })
public class PtyAddress {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_address_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_pty_address_client_address"))
    private ClientAddress clientAddress;

    @NotBlank(message = "PtyAddress's address should not be blank")
    @Column(name = "address", nullable = false, length = 250)
    private String address;

    @NotBlank(message = "PtyAddress's corregimiento should not be blank")
    @Column(name = "corregimiento", nullable = false, length = 100)
    private String corregimiento;

    @NotBlank(message = "PtyAddress's district should not be blank")
    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @NotBlank(message = "PtyAddress's province should not be blank")
    @Column(name = "province", nullable = false, length = 25)
    private String province;

    @NotBlank(message = "PtyAddress's country should not be blank")
    @Column(name = "country", nullable = false, length = 2)
    private String country = "PA";
}
