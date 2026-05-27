package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockMovementDTO(
        UUID id,
        UUID workOrderId,
        Integer quantity,
        MovementType type,
        LocalDateTime date
) {
}
