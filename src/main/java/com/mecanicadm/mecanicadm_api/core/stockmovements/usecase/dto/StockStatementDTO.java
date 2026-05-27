package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto;

import java.util.List;
import java.util.UUID;

public record StockStatementDTO(
        UUID materialId,
        Integer currentBalance,
        List<StockMovementDTO> movements
) {
}
