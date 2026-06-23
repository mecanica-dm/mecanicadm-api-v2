package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.infra.audit.AuditEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.hasText;

@Entity
@Table(name = "work_orders")
@SQLDelete(sql = "UPDATE work_orders SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class WorkOrder extends AuditEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "vehicle_id", nullable = false)
    private String vehicleId;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkOrderStatus status;

    @Column(name = "execution_start_at")
    private LocalDateTime executionStartAt;

    @Column(name = "execution_end_at")
    private LocalDateTime executionEndAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private Set<WorkOrderLaborItem> laborItems = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private Set<WorkOrderMaterialItem> materialItems = new HashSet<>();

    @OneToOne(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private WorkOrderBudget budget;

    protected WorkOrder() {
    }

    private WorkOrder(UUID clientId, String vehicleId, String description) {
        this.id = UUID.randomUUID();
        this.clientId = requireNonNull(clientId, "clientId cannot be null");
        this.vehicleId = requireNonNull(vehicleId, "vehicleId cannot be null");
        this.description = description;
        this.status = WorkOrderStatus.RECEIVED;
    }

    public static WorkOrder create(UUID clientId, String vehicleId, String description) {
        return new WorkOrder(clientId, vehicleId, description);
    }

    public void update(UUID clientId, String vehicleId, String description) {
        if (this.status != WorkOrderStatus.RECEIVED) {
            throw new WorkOrderExceptions.InvalidStatus(this.status.name());
        }

        if (nonNull(clientId)) {
            this.clientId = clientId;
        }

        if (hasText(vehicleId)) {
            this.vehicleId = vehicleId;
        }

        if (nonNull(description)) {
            this.description = description;
        }
    }

    public void assignBudget(WorkOrderBudget budget) {
        this.budget = requireNonNull(budget);
    }

    public void markAsDiagnosed() {
        if (this.status != WorkOrderStatus.RECEIVED) {
            throw new WorkOrderExceptions.InvalidStatusTransition(this.status.name(), WorkOrderStatus.DIAGNOSED.name());
        }
        this.status = WorkOrderStatus.DIAGNOSED;
    }

    public void markAsAwaitingExecution() {
        this.status = WorkOrderStatus.AWAITING_EXECUTION;
    }

    public void markAsInExecution() {
        if (this.status != WorkOrderStatus.AWAITING_EXECUTION) {
            throw new WorkOrderExceptions.InvalidStatusTransition(this.status.name(), WorkOrderStatus.IN_EXECUTION.name());
        }
        this.status = WorkOrderStatus.IN_EXECUTION;
        this.executionStartAt = LocalDateTime.now();
    }

    public void addLaborItem(WorkOrderLaborItem laborItem) {
        this.laborItems.add(requireNonNull(laborItem));
    }

    public void addMaterialItem(WorkOrderMaterialItem materialItem) {
        this.materialItems.add(requireNonNull(materialItem));
    }

    public void removeMaterialItem(UUID materialId) {
        this.materialItems.removeIf(item -> item.getMaterialId().equals(materialId));
    }

    public void startLaborItem(UUID laborItemId) {
        if (this.status != WorkOrderStatus.IN_EXECUTION) {
            throw new WorkOrderExceptions.LaborCannotStartIfNotInExecution();
        }
        findLaborItem(laborItemId)
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new)
                .startExecution();
    }

    public void markAsExecutionCompleted() {
        if (this.status != WorkOrderStatus.IN_EXECUTION) {
            throw new WorkOrderExceptions.InvalidStatusTransition(this.status.name(), WorkOrderStatus.EXECUTION_COMPLETED.name());
        }

        boolean hasLaborAwaitingExecution = laborItems.stream()
                .anyMatch(item -> item.getStatus() != LaborExecutionStatus.EXECUTION_COMPLETED);

        if (hasLaborAwaitingExecution) {
            throw new WorkOrderExceptions.PendingLaborItems();
        }

        this.status = WorkOrderStatus.EXECUTION_COMPLETED;
        this.executionEndAt = LocalDateTime.now();
    }

    public void markAsPaid() {
        if (this.status != WorkOrderStatus.EXECUTION_COMPLETED) {
            throw new WorkOrderExceptions.InvalidStatusTransition(this.status.name(), WorkOrderStatus.PAID.name());
        }
        this.status = WorkOrderStatus.PAID;
    }

    public void markAsDelivered() {
        if (this.status != WorkOrderStatus.PAID) {
            throw new WorkOrderExceptions.InvalidStatusTransition(this.status.name(), WorkOrderStatus.DELIVERED.name());
        }
        this.status = WorkOrderStatus.DELIVERED;
    }

    public Optional<WorkOrderLaborItem> findLaborItem(UUID laborItemId) {
        return this.laborItems.stream()
                .filter(item -> item.getId().equals(laborItemId))
                .findFirst();
    }

    public void markAsChangesRequested() {
        this.status = WorkOrderStatus.CHANGES_REQUESTED;
    }

    public void cancel() {
        this.status = WorkOrderStatus.CANCELLED;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getDescription() {
        return description;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public Optional<WorkOrderBudget> getBudget() {
        return Optional.ofNullable(budget);
    }

    public Optional<LocalDateTime> getExecutionStartAt() {
        return Optional.ofNullable(executionStartAt);
    }

    public Optional<LocalDateTime> getExecutionEndAt() {
        return Optional.ofNullable(executionEndAt);
    }

    public Set<WorkOrderLaborItem> getLaborItems() {
        return Collections.unmodifiableSet(laborItems);
    }

    public Set<WorkOrderMaterialItem> getMaterialItems() {
        return Collections.unmodifiableSet(materialItems);
    }
}
