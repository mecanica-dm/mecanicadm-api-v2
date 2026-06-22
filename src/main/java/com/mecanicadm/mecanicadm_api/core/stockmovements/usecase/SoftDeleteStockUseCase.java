package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.port.StockMovementsGateway;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

public class SoftDeleteStockUseCase implements VoidUseCase<SoftDeleteStockCommand> {

    private final StockMovementsGateway gateway;

    public SoftDeleteStockUseCase(StockMovementsGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(SoftDeleteStockCommand cmd) {
        StockMovements stock = gateway.findByMaterialIdAndWorkOrderId(cmd.materialId(), cmd.workOrderId())
                .orElseThrow(StockMovementsExceptions.NotFound::new);

        stock.delete();
        gateway.update(stock);
    }
}
