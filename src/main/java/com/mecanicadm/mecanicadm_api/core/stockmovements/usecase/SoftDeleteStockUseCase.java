package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;

public interface SoftDeleteStockUseCase {
    void handle(SoftDeleteStockCommand cmd);
}
