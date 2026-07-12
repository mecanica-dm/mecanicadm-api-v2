package com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.request;

import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateMaterialRequest(
        @Size(max = 255, message = "{validation.field.size.max}")
        String name,

        @Size(max = 255, message = "{validation.field.size.max}")
        String brand,

        @Size(max = 1000, message = "{validation.field.size.max}")
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
