package com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa;

import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.entity.ClientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientJpaEntity, UUID>, JpaSpecificationExecutor<ClientJpaEntity> {
    Optional<ClientJpaEntity> findClientByDocument(String document);

    Optional<ClientJpaEntity> findClientByEmail(String email);
}
