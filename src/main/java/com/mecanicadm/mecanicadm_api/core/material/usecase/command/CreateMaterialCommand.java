package com.mecanicadm.mecanicadm_api.core.material.usecase.command;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMaterialCommand(
        String name,

        String brand,

        String description,

        @NotNull(message = "{validation.material.price.required}")
        @Positive(message = "{validation.material.price.positive}")
        BigDecimal price,

        @NotNull(message = "{validation.material.type.required}")
        MaterialType type,

        @NotNull(message = "{validation.material.quantity.required}")
        @Positive(message = "{validation.material.quantity.positive}")
        int quantity
) {
}
