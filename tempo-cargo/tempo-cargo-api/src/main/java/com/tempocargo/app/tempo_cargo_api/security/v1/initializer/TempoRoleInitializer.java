package com.tempocargo.app.tempo_cargo_api.security.v1.initializer;

import com.tempocargo.app.tempo_cargo_api.auth.v1.role.model.TempoRole;
import com.tempocargo.app.tempo_cargo_api.auth.v1.role.repository.TempoRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
public class TempoRoleInitializer implements CommandLineRunner {

    private final TempoRoleRepository tempoRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Persist ADMIN TempoRole
        TempoRole adminRole = TempoRole.builder()
                .name("ADMIN")
                .build();
        tempoRoleRepository.save(adminRole);

        // Persist CUSTOMER TempoRole
        TempoRole customerRole = TempoRole.builder()
                .name("CUSTOMER")
                .build();
        tempoRoleRepository.save(customerRole);
    }
}
