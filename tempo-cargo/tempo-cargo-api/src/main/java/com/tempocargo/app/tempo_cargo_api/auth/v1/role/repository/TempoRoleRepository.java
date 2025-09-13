package com.tempocargo.app.tempo_cargo_api.auth.v1.role.repository;

import com.tempocargo.app.tempo_cargo_api.auth.v1.role.model.TempoRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempoRoleRepository extends JpaRepository<TempoRole, Long> {

    Optional<TempoRole> findByName(String name);

    boolean existsByName(String name);
}
