package com.mecanicadm.mecanicadm_api.infra.features.stockmovements.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.StockMovements;
import com.mecanicadm.mecanicadm_api.infra.features.stockmovements.persistence.entity.StockMovementsJpaEntity;

import static java.util.Objects.isNull;

public class StockMovementsJpaMapper {

    public static StockMovements toDomain(StockMovementsJpaEntity entity) {
        if (isNull(entity)) return null;
        return new StockMovements(entity.getId(),
                entity.getMaterialId(),
                entity.getWorkOrderId(),
                entity.getQuantity(),
                entity.getType()
        );
    }

    public static StockMovementsJpaEntity toEntity(StockMovements domain) {
        if (isNull(domain)) return null;
        return new StockMovementsJpaEntity(domain.getId(),
                domain.getMaterialId(),
                domain.getWorkOrderId(),
                domain.getQuantity(),
                domain.getType()
        );
    }
}
