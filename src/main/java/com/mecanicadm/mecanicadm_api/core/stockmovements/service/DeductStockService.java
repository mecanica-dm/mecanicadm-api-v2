package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.DeductStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.DeductStockCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeductStockService implements DeductStockUseCase {

    private final StockMovementsRepository stockMovementsRepository;

    public DeductStockService(StockMovementsRepository stockMovementsRepository) {
        this.stockMovementsRepository = stockMovementsRepository;
    }

    @Override
    @Transactional
    public void handle(DeductStockCommand cmd) {
        if (cmd.quantity() <= 0) {
            throw new StockMovementsExceptions.InvalidQuantity();
        }

        int currentBalance = stockMovementsRepository.getCurrentBalanceByMaterialId(cmd.materialId());
        if (currentBalance < cmd.quantity()) {
            throw new StockMovementsExceptions.InsufficientStock();
        }

        StockMovements stockMovements = StockMovements.recordReduction(cmd.materialId(), cmd.workOrderId(), cmd.quantity());
        stockMovementsRepository.save(stockMovements);
    }
}
