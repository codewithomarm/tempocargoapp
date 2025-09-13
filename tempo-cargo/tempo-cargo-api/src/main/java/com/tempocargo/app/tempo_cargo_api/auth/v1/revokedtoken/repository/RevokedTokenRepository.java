package com.tempocargo.app.tempo_cargo_api.auth.v1.revokedtoken.repository;

import com.tempocargo.app.tempo_cargo_api.auth.v1.revokedtoken.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

}
