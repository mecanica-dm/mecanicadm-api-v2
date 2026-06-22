package com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa;

import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.entity.ClientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientJpaEntity, UUID>, JpaSpecificationExecutor<ClientJpaEntity> {
    boolean existsByDocument(String document);

    boolean existsByEmail(String email);

    boolean existsByDocumentAndIdNot(String document, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
