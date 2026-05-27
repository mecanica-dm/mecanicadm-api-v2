package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "work_order_labor_items")
public class WorkOrderLaborItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "labor_id", nullable = false)
    private UUID laborId;

    @Column(name = "execution_start_at")
    private LocalDateTime executionStartAt;

    @Column(name = "execution_end_at")
    private LocalDateTime executionEndAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LaborExecutionStatus status;

    protected WorkOrderLaborItem() {
    }

    private WorkOrderLaborItem(UUID laborId) {
        this.laborId = requireNonNull(laborId);
        this.status = LaborExecutionStatus.AWAITING_EXECUTION;
    }

    public static WorkOrderLaborItem create(UUID laborId) {
        return new WorkOrderLaborItem(laborId);
    }

    public void startExecution() {
        if (this.status != LaborExecutionStatus.AWAITING_EXECUTION) {
            throw new WorkOrderExceptions.InvalidLaborStatusTransition(this.status.name(), LaborExecutionStatus.IN_EXECUTION.name());
        }
        this.status = LaborExecutionStatus.IN_EXECUTION;
        this.executionStartAt = LocalDateTime.now();
    }

    public void finishExecution() {
        if (this.status != LaborExecutionStatus.IN_EXECUTION) {
            throw new WorkOrderExceptions.InvalidLaborStatusTransition(this.status.name(), LaborExecutionStatus.EXECUTION_COMPLETED.name());
        }
        this.status = LaborExecutionStatus.EXECUTION_COMPLETED;
        this.executionEndAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getLaborId() {
        return laborId;
    }

    public LaborExecutionStatus getStatus() {
        return status;
    }

    public LocalDateTime getExecutionStartAt() {
        return executionStartAt;
    }

    public LocalDateTime getExecutionEndAt() {
        return executionEndAt;
    }
}
