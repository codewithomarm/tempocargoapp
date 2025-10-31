package com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseaddress.repository;

import com.tempocargo.app.tempo_cargo_api.depot.v1.warehouseaddress.model.WarehouseAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseAddressRepository extends JpaRepository<WarehouseAddress, Long> {

    List<WarehouseAddress> findByIsActiveTrue();

    Optional<WarehouseAddress> findByWarehouseName(String warehouseName);

    List<WarehouseAddress> findByWarehouseType_Code(String code);

    List<WarehouseAddress> findByWarehouseType_CodeAndIsActiveTrue(String code);

    boolean existsByWarehouseName(String warehouseName);
}
