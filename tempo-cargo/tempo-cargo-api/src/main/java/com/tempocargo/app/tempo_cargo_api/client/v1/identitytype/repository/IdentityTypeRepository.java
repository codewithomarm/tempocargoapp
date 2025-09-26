package com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.repository;

import com.tempocargo.app.tempo_cargo_api.client.v1.identitytype.model.IdentityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityTypeRepository extends JpaRepository<IdentityType, Long> {

}
