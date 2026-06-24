package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.infra.audit.AuditEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "work_orders")
@SQLRestriction("deleted_at IS NULL")
public class WorkOrderJpaEntity extends AuditEntity {

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
    private Set<WorkOrderLaborItemJpaEntity> laborItems = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private Set<WorkOrderMaterialItemJpaEntity> materialItems = new HashSet<>();

    @OneToOne(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private WorkOrderBudgetJpaEntity budget;

    protected WorkOrderJpaEntity() {
    }

    public WorkOrderJpaEntity(UUID id, UUID clientId, String vehicleId, String description, WorkOrderStatus status, LocalDateTime executionStartAt, LocalDateTime executionEndAt, Set<WorkOrderLaborItemJpaEntity> laborItems, Set<WorkOrderMaterialItemJpaEntity> materialItems, WorkOrderBudgetJpaEntity budget) {
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

    public Optional<WorkOrderBudgetJpaEntity> getBudget() {
        return Optional.ofNullable(budget);
    }

    public Optional<LocalDateTime> getExecutionStartAt() {
        return Optional.ofNullable(executionStartAt);
    }

    public Optional<LocalDateTime> getExecutionEndAt() {
        return Optional.ofNullable(executionEndAt);
    }

    public Set<WorkOrderLaborItemJpaEntity> getLaborItems() {
        return Collections.unmodifiableSet(laborItems);
    }

    public Set<WorkOrderMaterialItemJpaEntity> getMaterialItems() {
        return Collections.unmodifiableSet(materialItems);
    }
}
