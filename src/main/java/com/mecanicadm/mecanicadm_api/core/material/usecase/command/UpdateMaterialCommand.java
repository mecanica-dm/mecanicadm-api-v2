package com.mecanicadm.mecanicadm_api.core.material.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMaterialCommand(
        @JsonIgnore
        UUID id,

        @NotNull(message = "{validation.material.name.required}")
        String name,

        String brand,

        String description,

        @NotNull(message = "{validation.material.price.required}")
        @Positive(message = "{validation.material.price.positive}")
        BigDecimal price,

        @NotNull(message = "{validation.material.type.required}")
        MaterialType type
) {
    public UpdateMaterialCommand withId(UUID id) {
        return new UpdateMaterialCommand(id, name, brand, description, price, type);
    }
}
