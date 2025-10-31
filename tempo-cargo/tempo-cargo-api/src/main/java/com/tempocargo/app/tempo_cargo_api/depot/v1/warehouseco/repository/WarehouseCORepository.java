package com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseco.repository;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseco.model.WarehouseCO;
import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseus.model.WarehouseUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseCORepository extends JpaRepository<WarehouseCO, Long> {

    Optional<WarehouseUS> findByWarehouseAddress_Id(Long warehouseAddressId);
}
