package com.mecanicadm.mecanicadm_api.core.labor.usecase.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateLaborCommand(
        UUID id,
        String name,
        BigDecimal price
) {
    public UpdateLaborCommand withId(UUID id) {
        return new UpdateLaborCommand(id, name, price);
    }
}
