package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "work_order_budgets")
public class WorkOrderBudget {

    @Id
    @Column(name = "work_order_id", nullable = false, updatable = false)
    private UUID workOrderId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false, updatable = false)
    private WorkOrder workOrder;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkOrderBudgetStatus status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    protected WorkOrderBudget() {
    }

    private WorkOrderBudget(WorkOrder workOrder, BigDecimal totalPrice) {
        this.workOrder = requireNonNull(workOrder);
        this.totalPrice = requireNonNull(totalPrice);
        this.status = WorkOrderBudgetStatus.PENDING;
    }

    public static WorkOrderBudget create(WorkOrder workOrder, BigDecimal totalPrice) {
        return new WorkOrderBudget(workOrder, totalPrice);
    }

    public void updateTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = requireNonNull(totalPrice);
        this.status = WorkOrderBudgetStatus.PENDING;
        this.rejectionReason = null;
    }

    public void updateStatus(WorkOrderBudgetStatus status) {
        this.status = requireNonNull(status);
    }

    public void send() {
        this.status = WorkOrderBudgetStatus.WAITING_DECISION;
    }

    public void approve() {
        this.status = WorkOrderBudgetStatus.APPROVED;
        this.workOrder.markAsAwaitingExecution();
    }

    public void reject(String reason, boolean needsRevision) {
        this.rejectionReason = requireNonNull(reason);
        if (needsRevision) {
            this.status = WorkOrderBudgetStatus.CHANGES_REQUESTED;
            this.workOrder.markAsChangesRequested();
        } else {
            this.status = WorkOrderBudgetStatus.REJECTED;
            this.workOrder.cancel();
        }
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
