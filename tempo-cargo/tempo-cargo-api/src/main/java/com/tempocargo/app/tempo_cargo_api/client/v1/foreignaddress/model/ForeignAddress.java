package com.tempocargo.app.tempo_cargo_api.client.v1.foreignaddress.model;

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
@Table(schema = "client", name = "foreign_address",
        indexes = {
            @Index(columnList = "client_address_id", name = "fk_foreign_address_client_address_idx")
        })
public class ForeignAddress {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_address_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_foreign_address_client_address"))
    private ClientAddress clientAddress;

    @NotBlank(message = "ForeignAddress's streetLine1 should not be blank")
    @Column(name = "street_line_1", nullable = false, length = 200)
    private String streetLine1;

    @Column(name = "street_line_2", length = 100)
    private String streetLine2;

    @NotBlank(message = "ForeignAddress's city should not be blank")
    @Column(name = "city", nullable = false, length = 85)
    private String city;

    @NotBlank(message = "ForeignAddress's stateProvince should not be blank")
    @Column(name = "state_province", nullable = false, length = 50)
    private String stateProvince;

    @NotBlank(message = "ForeginAddress's postalCode should not be blank")
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @NotBlank(message = "ForeignAddress's countryCode should not be blank")
    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;
}
