package com.mecanicadm.mecanicadm_api.core.client.adapter.repository;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    Optional<Client> findClientByDocument(String document);
    Optional<Client> findClientByEmail(String email);
}
