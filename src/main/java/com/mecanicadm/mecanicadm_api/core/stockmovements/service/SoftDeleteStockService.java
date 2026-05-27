package com.mecanicadm.mecanicadm_api.core.stockmovements.service;

import com.mecanicadm.mecanicadm_api.core.stockmovements.adapter.repository.StockMovementsRepository;
import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.core.stockmovements.exception.StockMovementsExceptions;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.SoftDeleteStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoftDeleteStockService implements SoftDeleteStockUseCase {

    private final StockMovementsRepository repository;

    public SoftDeleteStockService(StockMovementsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void handle(SoftDeleteStockCommand cmd) {
        StockMovements stock = repository.findByMaterialIdAndWorkOrderId(cmd.materialId(), cmd.workOrderId())
                .orElseThrow(StockMovementsExceptions.NotFound::new);

        repository.delete(stock);
    }
}
