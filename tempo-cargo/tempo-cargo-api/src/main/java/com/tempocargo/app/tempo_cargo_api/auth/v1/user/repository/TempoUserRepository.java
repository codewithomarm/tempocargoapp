package com.tempocargo.app.tempo_cargo_api.auth.v1.user.repository;

import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempoUserRepository extends JpaRepository<TempoUser, Long> {

    Optional<TempoUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}