package com.mecanicadm.mecanicadm_api.core.workorders.domain;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.LaborExecutionStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.hasText;

public class WorkOrder {

    private UUID id;

    private UUID clientId;

    private String vehicleId;

    private String description;

    private WorkOrderStatus status;

    private LocalDateTime executionStartAt;

    private LocalDateTime executionEndAt;

    private Set<WorkOrderLaborItem> laborItems = new HashSet<>();

    private Set<WorkOrderMaterialItem> materialItems = new HashSet<>();

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

    public WorkOrder(UUID id, UUID clientId, String vehicleId, String description, WorkOrderStatus status, LocalDateTime executionStartAt, LocalDateTime executionEndAt, Set<WorkOrderLaborItem> laborItems, Set<WorkOrderMaterialItem> materialItems, WorkOrderBudget budget) {
        this.id = id;
        this.clientId = clientId;
        this.vehicleId = vehicleId;
        this.description = description;
        this.status = status;
        this.executionStartAt = executionStartAt;
        this.executionEndAt = executionEndAt;
        this.laborItems = laborItems;
        this.materialItems = materialItems;
        this.budget = budget;
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
