package com.mecanicadm.mecanicadm_api.core.material.usecase.command;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMaterialCommand(
        UUID id,
        String name,
        String brand,
        String description,
        BigDecimal price,
        MaterialType type
) {
    public UpdateMaterialCommand withId(UUID id) {
        return new UpdateMaterialCommand(id, name, brand, description, price, type);
    }
}
