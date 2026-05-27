package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.DeductStockCommand;

public interface DeductStockUseCase {
    void handle(DeductStockCommand cmd);
}
