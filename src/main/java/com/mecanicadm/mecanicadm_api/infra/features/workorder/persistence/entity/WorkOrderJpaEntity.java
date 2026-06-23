package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderStatus;
import com.mecanicadm.mecanicadm_api.infra.baseentities.AuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "work_orders")
@SQLDelete(sql = "UPDATE work_orders SET deleted_at = now() WHERE id = ?")
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

    private WorkOrderJpaEntity(UUID clientId, String vehicleId, String description) {
        this.id = UUID.randomUUID();
        this.clientId = requireNonNull(clientId, "clientId cannot be null");
        this.vehicleId = requireNonNull(vehicleId, "vehicleId cannot be null");
        this.description = description;
        this.status = WorkOrderStatus.RECEIVED;
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
