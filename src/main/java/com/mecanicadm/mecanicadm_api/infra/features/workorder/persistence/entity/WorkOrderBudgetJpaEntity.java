package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "work_order_budgets")
public class WorkOrderBudgetJpaEntity {

    @Id
    @Column(name = "work_order_id", nullable = false, updatable = false)
    private UUID workOrderId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false, updatable = false)
    private WorkOrderJpaEntity workOrder;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkOrderBudgetStatus status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    protected WorkOrderBudgetJpaEntity() {
    }

    public WorkOrderBudgetJpaEntity(UUID workOrderId, BigDecimal totalPrice, WorkOrderBudgetStatus status, String rejectionReason) {
        this.workOrderId = workOrderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    private WorkOrderBudgetJpaEntity(WorkOrderJpaEntity workOrder, BigDecimal totalPrice) {
        this.workOrder = requireNonNull(workOrder);
        this.totalPrice = requireNonNull(totalPrice);
        this.status = WorkOrderBudgetStatus.PENDING;
    }

    public UUID getWorkOrderId() {
        return workOrderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public WorkOrderBudgetStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
