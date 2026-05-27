package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import jakarta.persistence.*;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "work_order_material_items")
public class WorkOrderMaterialItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "material_id", nullable = false)
    private UUID materialId;

    @Column(name = "quantity", nullable = false)
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

    public static WorkOrderMaterialItem create(UUID materialId, int quantity) {
        return new WorkOrderMaterialItem(materialId, quantity);
    }

    public UUID getMaterialId() {
        return materialId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
