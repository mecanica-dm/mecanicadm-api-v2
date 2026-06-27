package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "work_order_labor_items")
public class WorkOrderLaborItemJpaEntity {

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

    protected WorkOrderLaborItemJpaEntity() {
    }

    public WorkOrderLaborItemJpaEntity(UUID id, UUID laborId, LocalDateTime executionStartAt, LocalDateTime executionEndAt, LaborExecutionStatus status) {
        this.id = id;
        this.laborId = laborId;
        this.executionStartAt = executionStartAt;
        this.executionEndAt = executionEndAt;
        this.status = status;
    }

    private WorkOrderLaborItemJpaEntity(UUID laborId) {
        this.laborId = requireNonNull(laborId);
        this.status = LaborExecutionStatus.AWAITING_EXECUTION;
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
