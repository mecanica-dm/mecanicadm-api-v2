package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateLaborRequest(
        @NotBlank(message = "{validation.labor.name.not.blank}")
        @Size(max = 255, message = "{validation.field.size.max}")
        String name,

        @NotNull(message = "{validation.labor.price.not.null}")
        BigDecimal price
) {
}

