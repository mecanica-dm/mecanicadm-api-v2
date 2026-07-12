package com.mecanicadm.mecanicadm_api.infra.features.material.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mecanicadm.mecanicadm_api.core.material.domain.enums.MaterialType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMaterialRequest(
        @JsonIgnore
        UUID id,

        @NotNull(message = "{validation.material.name.required}")
        @Size(max = 255, message = "{validation.material.name.size}")
        String name,

        @Size(max = 255, message = "{validation.material.brand.size}")
        String brand,

        @Size(max = 1000, message = "{validation.material.description.size}")
        String description,

        @NotNull(message = "{validation.material.price.required}")
        @Positive(message = "{validation.material.price.positive}")
        @DecimalMax(value = "999999.99", message = "{validation.material.price.max}")
        BigDecimal price,

        @NotNull(message = "{validation.material.type.required}")
        MaterialType type
) {
    public UpdateMaterialRequest withId(UUID id) {
        return new UpdateMaterialRequest(id, name, brand, description, price, type);
    }
}
