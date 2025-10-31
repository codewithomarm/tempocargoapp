package com.tempocargo.app.tempo_cargo_api.depot.v1.warehousetype.repository;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehousetype.model.WarehouseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseTypeRepository extends JpaRepository<WarehouseType, Long> {

    Optional<WarehouseType> findByCode(String code);

    boolean existsByCode(String code);

    Optional<WarehouseType> findByDisplayName(String displayName);
}
