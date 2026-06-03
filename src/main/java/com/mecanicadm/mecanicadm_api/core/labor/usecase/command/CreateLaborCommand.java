package com.mecanicadm.mecanicadm_api.core.labor.usecase.command;

import java.math.BigDecimal;

public record CreateLaborCommand(
        String name,
        BigDecimal price
) {
}
