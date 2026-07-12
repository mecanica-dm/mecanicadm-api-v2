package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateLaborRequest(
        @NotBlank(message = "{validation.labor.name.not.blank}")
        @Size(max = 255, message = "{validation.labor.name.size}")
        String name,

        @NotNull(message = "{validation.labor.price.not.null}")
        @DecimalMax(value = "999999.99", message = "{validation.labor.price.max}")
        BigDecimal price
) {
}

