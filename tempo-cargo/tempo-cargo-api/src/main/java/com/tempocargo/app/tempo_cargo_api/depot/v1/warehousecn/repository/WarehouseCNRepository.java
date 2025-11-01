package com.tempocargo.app.tempo_cargo_api.depot.v1.warehousecn.repository;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehousecn.model.WarehouseCN;
import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseus.model.WarehouseUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseCNRepository extends JpaRepository<WarehouseCN, Long> {

    Optional<WarehouseUS> findByWarehouseAddress_Id(Long warehouseAddressId);
}
