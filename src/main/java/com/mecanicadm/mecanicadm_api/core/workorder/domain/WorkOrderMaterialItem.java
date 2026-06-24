package com.mecanicadm.mecanicadm_api.core.workorder.domain;

import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class WorkOrderMaterialItem {

    private UUID id;

    private UUID materialId;

    private int quantity;

    protected WorkOrderMaterialItem() {
    }

    private WorkOrderMaterialItem(UUID materialId, int quantity) {
        this.materialId = requireNonNull(materialId);
        if (quantity <= 0) {
            throw new WorkOrderExceptions.InvalidMaterialQuantity();
        }
        this.quantity = quantity;
    }

    private WorkOrderMaterialItem(UUID id, UUID materialId, int quantity) {
        this.id = id;
        this.materialId = materialId;
        this.quantity = quantity;
    }

    public static WorkOrderMaterialItem create(UUID materialId, int quantity) {
        return new WorkOrderMaterialItem(materialId, quantity);
    }

    public static WorkOrderMaterialItem restore(UUID id, UUID materialId, int quantity) {
        return new WorkOrderMaterialItem(id, materialId, quantity);
    }

    public UUID getId() {
        return id;
    }

    public UUID getMaterialId() {
        return materialId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
