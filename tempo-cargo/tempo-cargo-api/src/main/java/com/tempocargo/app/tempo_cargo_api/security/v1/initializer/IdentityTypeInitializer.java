package com.tempocargo.app.tempo_cargo_api.security.v1.initializer;

import com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.model.IdentityType;
import com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.repository.IdentityTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(3)
public class IdentityTypeInitializer implements CommandLineRunner {

    private final IdentityTypeRepository identityTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        identityTypeRepository.save(IdentityType.builder()
                .code("PANA")
                .displayName("PANAMEÑO NACIONAL")
                .build());

        identityTypeRepository.save(IdentityType.builder()
                .code("EXTR")
                .displayName("EXTRANJERO RESIDENTE")
                .build());

        identityTypeRepository.save(IdentityType.builder()
                .code("NATU")
                .displayName("PANA. NATURALIZADO")
                .build());

        identityTypeRepository.save(IdentityType.builder()
                .code("PAEX")
                .displayName("PANA. EN EXTRANJERO")
                .build());

        identityTypeRepository.save(IdentityType.builder()
                .code("PAIN")
                .displayName("PANA. INDIGENA")
                .build());
    }
}
