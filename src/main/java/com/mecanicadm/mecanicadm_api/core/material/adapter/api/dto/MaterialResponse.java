package com.mecanicadm.mecanicadm_api.core.material.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MaterialResponse(
        UUID id,
        String name,
        String brand,
        String description,
        BigDecimal price,
        MaterialType type,
        LocalDateTime dateCreated,
        LocalDateTime dateUpdated
) {
    public MaterialResponse(Material material) {
        this(
                material.getId(),
                material.getName(),
                material.getBrand(),
                material.getDescription(),
                material.getPrice(),
                material.getType(),
                material.getDateCreated(),
                material.getDateUpdated()
        );
    }
}
