package com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;

import java.math.BigDecimal;
import java.util.UUID;

public record MaterialResponse(
        UUID id,
        String name,
        String brand,
        String description,
        BigDecimal price,
        MaterialType type
) {
    public MaterialResponse(Material material) {
        this(
                material.getId(),
                material.getName(),
                material.getBrand(),
                material.getDescription(),
                material.getPrice(),
                material.getType()
        );
    }
}
