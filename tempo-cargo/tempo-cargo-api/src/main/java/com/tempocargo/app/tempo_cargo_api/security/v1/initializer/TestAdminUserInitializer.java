package com.tempocargo.app.tempo_cargo_api.security.v1.initializer;

import com.tempocargo.app.tempo_cargo_api.auth.v1.role.model.TempoRole;
import com.tempocargo.app.tempo_cargo_api.auth.v1.role.repository.TempoRoleRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.model.TempoRoleUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.repository.TempoRoleUserRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.repository.TempoUserRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import com.tempocargo.app.tempo_cargo_api.client.v1.client.repository.ClientRepository;
import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.model.ClientType;
import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.repository.ClientTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(4)
public class TestAdminUserInitializer implements CommandLineRunner {

    private final TempoUserRepository tempoUserRepository;
    private final TempoRoleRepository tempoRoleRepository;
    private final TempoRoleUserRepository tempoRoleUserRepository;
    private final ClientRepository clientRepository;
    private final ClientTypeRepository clientTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Find ClientType in DB
        ClientType clientType = clientTypeRepository.findByCode("INDV")
                .orElseThrow(() -> new RuntimeException("INDV clientType not found"));

        // Persist TestClient Client
        Client testClient = Client.builder()
                .poBoxNumber(12345)
                .phoneNumberPrimary("+507 6616-2729")
                .type(clientType)
                .build();
        Client entityTestClient = clientRepository.save(testClient);

        // Persist testUser TempoUser
        TempoUser testUser = TempoUser.builder()
                .client(entityTestClient)
                .username("codewithomarm")
                .passwordHash(passwordEncoder.encode("password123"))
                .email("codewithomarm@gmail.com")
                .build();
        TempoUser entityTestUser = tempoUserRepository.save(testUser);

        // Find Role in DB
        TempoRole role = tempoRoleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN Role not found"));

        // Persist testUser-Admin relationship
        TempoRoleUser testRoleUser = TempoRoleUser.builder()
                .role(role)
                .user(entityTestUser)
                .build();
        tempoRoleUserRepository.save(testRoleUser);
    }
}
