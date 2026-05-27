package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command;

import java.util.UUID;

public record DeductStockCommand(UUID materialId, UUID workOrderId, int quantity) { }
