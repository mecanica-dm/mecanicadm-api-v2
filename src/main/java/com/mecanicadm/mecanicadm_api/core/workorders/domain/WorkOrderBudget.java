package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class WorkOrderBudget {

    private UUID workOrderId;

    private WorkOrder workOrder;

    private BigDecimal totalPrice;

    private WorkOrderBudgetStatus status;

    private String rejectionReason;

    protected WorkOrderBudget() {
    }

    public WorkOrderBudget(UUID workOrderId, BigDecimal totalPrice, WorkOrderBudgetStatus status, String rejectionReason) {
        this.workOrderId = workOrderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.rejectionReason = rejectionReason;
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
