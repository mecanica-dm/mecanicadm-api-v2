package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.AddStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.AddStockCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddStockService implements AddStockUseCase {

    private final StockMovementsRepository stockMovementsRepository;

    public AddStockService(StockMovementsRepository stockMovementsRepository) {
        this.stockMovementsRepository = stockMovementsRepository;
    }

    @Override
    @Transactional
    public void handle(AddStockCommand cmd) {
        if (cmd.quantity() <= 0) {
            throw new StockMovementsExceptions.InvalidQuantity();
        }

        StockMovements stockMovements = StockMovements.recordAddition(cmd.materialId(), cmd.quantity());
        stockMovementsRepository.save(stockMovements);
    }
}
