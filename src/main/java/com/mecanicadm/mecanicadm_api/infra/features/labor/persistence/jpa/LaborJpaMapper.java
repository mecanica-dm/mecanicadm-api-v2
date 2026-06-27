package com.mecanicadm.mecanicadm_api.infra.features.labor.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.infra.features.labor.persistence.entity.LaborJpaEntity;

import static java.util.Objects.isNull;

public class LaborJpaMapper {

    private LaborJpaMapper() {
    }

    public static Labor toDomain(LaborJpaEntity entity) {
        if (isNull(entity)) {
            return null;
        }

        return Labor.restore(
                entity.getId(),
                entity.getName(),
                entity.getPrice()
        );
    }

    public static LaborJpaEntity toEntity(Labor domain) {
        if (isNull(domain)) {
            return null;
        }

        return new LaborJpaEntity(domain.getId(), domain.getName(), domain.getPrice());
    }
}

