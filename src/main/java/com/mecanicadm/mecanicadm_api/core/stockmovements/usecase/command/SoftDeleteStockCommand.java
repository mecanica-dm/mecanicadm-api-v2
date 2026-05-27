package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SoftDeleteStockCommand(
        @NotNull(message = "{validation.stock.materialId.not.null}")
        UUID materialId,
        UUID workOrderId
) {
}
