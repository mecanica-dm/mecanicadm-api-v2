package com.mecanicadm.mecanicadm_api.core.stockmovements.domain;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;

import java.util.UUID;

public class StockMovements {

    private UUID id;

    private UUID workOrderId;

    private UUID materialId;

    private Integer quantity;

    private MovementType type;

    public StockMovements() {
    }

    public StockMovements(UUID id, UUID materialId, UUID workOrderId, Integer quantity, MovementType type) {
        this.id = id;
        this.materialId = materialId;
        this.workOrderId = workOrderId;
        this.quantity = quantity;
        this.type = type;
    }

    public static StockMovements recordAddition(UUID materialId, Integer amount) {
        return new StockMovements(null, materialId, null, amount, MovementType.ADDITION);
    }

    public static StockMovements recordReduction(UUID materialId, UUID workOrderId, Integer amount) {
        return new StockMovements(null, materialId, workOrderId, amount, MovementType.REDUCTION);
    }

    public UUID getId() {
        return id;
    }

    public UUID getWorkOrderId() {
        return workOrderId;
    }

    public UUID getMaterialId() {
        return materialId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public MovementType getType() {
        return type;
    }
}
