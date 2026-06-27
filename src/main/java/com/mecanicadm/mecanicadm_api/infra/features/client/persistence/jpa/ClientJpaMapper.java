package com.mecanicadm.mecanicadm_api.infra.features.client.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.client.domain.Client;
import com.mecanicadm.mecanicadm_api.infra.features.client.persistence.entity.ClientJpaEntity;

import static java.util.Objects.isNull;

public class ClientJpaMapper {

    public static Client toDomain(ClientJpaEntity entity) {
        if (isNull(entity)) return null;
        return new Client(entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getDocument(),
                entity.getPhone()
        );
    }

    public static ClientJpaEntity toEntity(Client domain) {
        if (isNull(domain)) return null;
        return new ClientJpaEntity(domain.getId(),
                domain.getName(),
                domain.getEmail(),
                domain.getDocument(),
                domain.getPhone()
        );
    }
}
