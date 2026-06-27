package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class WorkOrderLaborItem {

    private UUID id;

    private UUID laborId;

    private LocalDateTime executionStartAt;

    private LocalDateTime executionEndAt;

    private LaborExecutionStatus status;

    protected WorkOrderLaborItem() {
    }

    public WorkOrderLaborItem(UUID id, UUID laborId, LocalDateTime executionStartAt, LocalDateTime executionEndAt, LaborExecutionStatus status) {
        this.id = id;
        this.laborId = laborId;
        this.executionStartAt = executionStartAt;
        this.executionEndAt = executionEndAt;
        this.status = status;
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
