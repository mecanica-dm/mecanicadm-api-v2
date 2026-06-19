package com.mecanicadm.mecanicadm_api.infra.features.stockmovements.persistence.entity;

import com.mecanicadm.mecanicadm_api.core.stockmovements.domain.enums.MovementType;
import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "stock_movements")
@SQLDelete(sql = "UPDATE stock_movements SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class StockMovementsJpaEntity extends AuditEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "work_order_id")
    private UUID workOrderId;

    @Column(name = "material_id", nullable = false)
    private UUID materialId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MovementType type;

    public StockMovementsJpaEntity() {
    }

    public StockMovementsJpaEntity(UUID id, UUID materialId, UUID workOrderId, Integer quantity, MovementType type) {
        this.id = id;
        this.materialId = materialId;
        this.workOrderId = workOrderId;
        this.quantity = quantity;
        this.type = type;
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
