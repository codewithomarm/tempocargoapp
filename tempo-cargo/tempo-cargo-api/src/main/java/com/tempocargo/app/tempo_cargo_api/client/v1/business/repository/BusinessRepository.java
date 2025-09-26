package com.tempocargo.app.tempo_cargo_api.client.v1.business.repository;

import com.tempocargo.app.tempo_cargo_api.client.v1.business.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b.nameCode FROM Business b WHERE b.nameCode LIKE CONCAT(:baseName, '%')")
    List<String> findAllByNameCodeStartingWith(@Param("baseName") String baseName);

    boolean existsByName(String name);

    boolean existsByRucNumber(String rucNumber);
}
