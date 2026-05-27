package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command;

import java.util.UUID;

public record AddStockCommand(UUID materialId, int quantity) { }
