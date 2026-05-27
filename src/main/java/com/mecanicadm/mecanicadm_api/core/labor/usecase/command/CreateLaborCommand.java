package com.mecanicadm.mecanicadm_api.core.labor.usecase.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateLaborCommand(
        @NotBlank(message = "{validation.labor.name.not.blank}")
        String name,

        @NotNull(message = "{validation.labor.price.not.null}")
        BigDecimal price
) {
}
