package com.mecanicadm.mecanicadm_api.infra.features.material.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.infra.features.material.persistence.entity.MaterialJpaEntity;

import static java.util.Objects.isNull;

public class MaterialJpaMapper {

    private MaterialJpaMapper() {
    }

    public static Material toDomain(MaterialJpaEntity entity) {
        if (isNull(entity)) {
            return null;
        }

        return Material.restore(entity.getId(), entity.getName(), entity.getBrand(), entity.getDescription(), entity.getPrice(), entity.getType());
    }

    public static MaterialJpaEntity toEntity(Material domain) {
        if (isNull(domain)) {
            return null;
        }

        return new MaterialJpaEntity(domain.getId(), domain.getName(), domain.getBrand(), domain.getDescription(), domain.getPrice(), domain.getType());
    }
}
