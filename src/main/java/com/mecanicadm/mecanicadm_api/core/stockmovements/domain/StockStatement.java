package com.mecanicadm.mecanicadm_api.core.stockmovements.domain;

import java.util.List;
import java.util.UUID;

public record StockStatement(
        UUID materialId,
        Integer currentBalance,
        List<StockMovements> movements
) {
}
