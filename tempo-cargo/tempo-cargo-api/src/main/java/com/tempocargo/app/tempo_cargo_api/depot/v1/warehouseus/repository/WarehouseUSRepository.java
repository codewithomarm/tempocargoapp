package com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseus.repository;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseus.model.WarehouseUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseUSRepository extends JpaRepository<WarehouseUS, Long> {

    Optional<WarehouseUS> findByWarehouseAddress_Id(Long warehouseAddressId);
}
