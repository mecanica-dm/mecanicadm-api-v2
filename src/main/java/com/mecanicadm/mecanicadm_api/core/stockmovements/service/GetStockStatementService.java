package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.GetStockStatementUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto.StockMovementDTO;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.dto.StockStatementDTO;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.query.GetStockStatementQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetStockStatementService implements GetStockStatementUseCase {

    private final StockMovementsRepository stockMovementsRepository;

    public GetStockStatementService(StockMovementsRepository stockMovementsRepository) {
        this.stockMovementsRepository = stockMovementsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public StockStatementDTO handle(GetStockStatementQuery query) {
        Integer currentBalance = stockMovementsRepository.getCurrentBalanceByMaterialId(query.materialId());
        List<StockMovements> movements = stockMovementsRepository.findAllByMaterialIdOrderByDateCreatedDesc(query.materialId());

        List<StockMovementDTO> movementDTOs = movements.stream()
                .map(sm -> new StockMovementDTO(
                        sm.getId(),
                        sm.getWorkOrderId(),
                        sm.getQuantity(),
                        sm.getType(),
                        sm.getDateCreated()
                ))
                .toList();

        return new StockStatementDTO(
                query.materialId(),
                currentBalance,
                movementDTOs
        );
    }
}
