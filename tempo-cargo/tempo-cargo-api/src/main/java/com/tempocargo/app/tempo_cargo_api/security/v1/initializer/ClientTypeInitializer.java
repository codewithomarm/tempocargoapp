package com.tempocargo.app.tempo_cargo_api.security.v1.initializer;

import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.model.ClientType;
import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.repository.ClientTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class ClientTypeInitializer implements CommandLineRunner {

    private final ClientTypeRepository clientTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Persist INDV ClientType
        ClientType clientTypeIndividual = ClientType.builder()
                .code("INDV")
                .displayName("INDIVIDUAL")
                .build();
        clientTypeRepository.save(clientTypeIndividual);

        // Persist BUSN ClientType
        ClientType clientType = ClientType.builder()
                .code("BUSN")
                .displayName("BUSINESS")
                .build();
        clientTypeRepository.save(clientType);
    }
}
