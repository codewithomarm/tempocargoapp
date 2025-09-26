package com.tempocargo.app.tempo_cargo_api.client.v1.individual.repository;

import com.tempocargo.app.tempo_cargo_api.client.v1.individual.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndividualRepository extends JpaRepository<Individual, Long> {

    @Query("SELECT i.nameCode FROM Individual i WHERE i.nameCode LIKE CONCAT(:baseName, '%')")
    List<String> findAllByNameCodeStartingWith(@Param("baseName") String baseName);

    boolean existsByIdentityNumber(String identityNumber);
}
