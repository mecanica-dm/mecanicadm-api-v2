package com.mecanicadm.mecanicadm_api.core.labor.adapter.api.dto;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LaborResponse(
        UUID id,
        String name,
        BigDecimal price,
        LocalDateTime dateCreated,
        LocalDateTime dateUpdated
) {

    public LaborResponse(Labor labor) {
        this(
                labor.getId(),
                labor.getName(),
                labor.getPrice(),
                labor.getDateCreated(),
                labor.getDateUpdated()
        );
    }
}
