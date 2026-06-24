package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "work_order_material_items")
public class WorkOrderMaterialItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "material_id", nullable = false)
    private UUID materialId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    protected WorkOrderMaterialItemJpaEntity() {
    }

    public WorkOrderMaterialItemJpaEntity(UUID id, UUID materialId, int quantity) {
        this.id = id;
        this.materialId = materialId;
        this.quantity = quantity;
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
