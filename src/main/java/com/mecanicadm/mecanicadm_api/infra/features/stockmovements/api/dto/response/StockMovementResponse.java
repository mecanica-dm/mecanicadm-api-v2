package com.mecanicadm.mecanicadm_api.infra.features.stockmovements.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;

import java.util.UUID;

public record StockMovementResponse(
        UUID id,
        UUID workOrderId,
        Integer quantity,
        MovementType type
//        LocalDateTime date
) {
}
