package com.mecanicadm.mecanicadm_api.core.stockmovements.usecase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.port.StockMovementsFilter;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.port.StockMovementsGateway;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.query.GetStockStatementQuery;
import com.mecanicadm.mecanicadm_api.infra.features.stockmovements.api.dto.response.StockMovementResponse;
import com.mecanicadm.mecanicadm_api.infra.features.stockmovements.api.dto.response.StockStatementResponse;

import java.util.List;

public class GetStockStatementUseCase {

    private final StockMovementsGateway gateway;

    public GetStockStatementUseCase(StockMovementsGateway gateway) {
        this.gateway = gateway;
    }

    public StockStatementResponse execute(GetStockStatementQuery query) {
        Integer currentBalance = gateway.getCurrentBalanceByMaterialId(query.materialId());

        StockMovementsFilter filter = new StockMovementsFilter(query.materialId());
        List<StockMovements> movements = gateway.findAllByMaterialIdOrderByDateCreatedDesc(filter);

        List<StockMovementResponse> movementDTOs = movements.stream()
                .map(sm -> new StockMovementResponse(
                        sm.getId(),
                        sm.getWorkOrderId(),
                        sm.getQuantity(),
                        sm.getType()
//                        sm.getDateCreated()
                ))
                .toList();

        return new StockStatementResponse(
                query.materialId(),
                currentBalance,
                movementDTOs
        );
    }
}
