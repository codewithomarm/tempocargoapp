package com.tempocargo.app.tempo_cargo_api.client.v1.client.repository;

import com.tempocargo.app.tempo_cargo_api.client.v1.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
