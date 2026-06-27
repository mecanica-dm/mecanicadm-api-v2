package com.mecanicadm.mecanicadm_api.infra.features.stockmovements.api.dto.response;

import java.util.List;
import java.util.UUID;

public record StockStatementResponse(
        UUID materialId,
        Integer currentBalance,
        List<StockMovementResponse> movements
) {
}
