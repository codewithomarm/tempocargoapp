package com.tempocargo.app.tempo_cargo_api.client.v1.client.repository;

import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT nextval('client.po_box_seq')", nativeQuery = true)
    Long getNextPoBoxNumber();
}
