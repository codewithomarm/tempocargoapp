package com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.repository;

import com.tempocargo.app.tempo_cargo_api.client.v1.clienttype.model.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientTypeRepository extends JpaRepository<ClientType, Long> {

    Optional<ClientType> findByCode(String code);

    Optional<ClientType> findByDisplayName(String displayName);
}
