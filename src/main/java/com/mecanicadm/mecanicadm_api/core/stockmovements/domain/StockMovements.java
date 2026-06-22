package com.mecanicadm.mecanicadm_api.core.stockmovements.domain;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;
import com.mecanicadm.mecanicadm_api.shared.domain.AuditDomain;

import java.time.LocalDateTime;
import java.util.UUID;

public class StockMovements extends AuditDomain {

    private UUID id;

    private UUID workOrderId;

    private UUID materialId;

    private Integer quantity;

    private MovementType type;

    private StockMovements(UUID id, UUID materialId, UUID workOrderId, Integer quantity, MovementType type,
                           LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.materialId = materialId;
        this.workOrderId = workOrderId;
        this.quantity = quantity;
        this.type = type;
        this.deletedAt = deletedAt;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public static StockMovements recordAddition(UUID materialId, Integer amount) {
        var movement = new StockMovements(null, materialId, null, amount, MovementType.ADDITION, null, null, null);
        movement.create();
        return movement;
    }

    public static StockMovements restore(UUID id, UUID materialId, UUID workOrderId, Integer quantity, MovementType type,
                                          LocalDateTime deletedAt, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        return new StockMovements(id, materialId, workOrderId, quantity, type, deletedAt, dateCreated, dateUpdated);
    }

    public static StockMovements recordReduction(UUID materialId, UUID workOrderId, Integer amount) {
        var movement = new StockMovements(null, materialId, workOrderId, amount, MovementType.REDUCTION, null, null, null);
        movement.create();
        return movement;
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
