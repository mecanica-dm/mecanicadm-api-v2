package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.AddStockCommand;

public interface AddStockUseCase {
    void handle(AddStockCommand cmd);
}
