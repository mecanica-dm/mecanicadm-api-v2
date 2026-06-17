package com.mecanicadm.mecanicadm_api.core.stockmovements.domain.port;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockMovementsGateway {
    StockMovements create(StockMovements stockMovements);

    void delete(StockMovements stockMovements);

    List<StockMovements> findAllByMaterialIdOrderByDateCreatedDesc(StockMovementsFilter query);

    int getCurrentBalanceByMaterialId(UUID materialId);

    Optional<StockMovements> findByMaterialIdAndWorkOrderId(UUID materialId, UUID workOrderId);

    Optional<StockMovements> findByMaterialId(UUID materialId);
}
