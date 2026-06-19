package com.mecanicadm.mecanicadm_api.infra.features.labor.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;

import java.math.BigDecimal;
import java.util.UUID;

public record LaborResponse(
        UUID id,
        String name,
        BigDecimal price
) {
    public LaborResponse(Labor labor) {
        this(
                labor.getId(),
                labor.getName(),
                labor.getPrice()
        );
    }
}

