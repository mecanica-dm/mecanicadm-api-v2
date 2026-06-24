package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderJpaEntity;

import static java.util.Objects.isNull;

public class WorkOrderJpaMapper {

    public static WorkOrder toDomain(WorkOrderJpaEntity entity) {
        if (isNull(entity)) return null;
        return WorkOrder.restore(
                entity.getId(),
                entity.getClientId(),
                entity.getVehicleId(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getExecutionStartAt().orElse(null),
                entity.getExecutionEndAt().orElse(null),
                WorkOrderLaborItemJpaMapper.toDomainSet(entity.getLaborItems()),
                WorkOrderMaterialItemJpaMapper.toDomainSet(entity.getMaterialItems()),
                WorkOrderBudgetJpaMapper.toDomain(entity.getBudget().orElse(null)),
                entity.getDateCreated(),
                entity.getDateUpdated(),
                entity.getDeletedAt()
        );
    }

    public static WorkOrderJpaEntity toEntity(WorkOrder domain) {
        if (isNull(domain)) return null;
        WorkOrderJpaEntity entity = new WorkOrderJpaEntity(
                domain.getId(),
                domain.getClientId(),
                domain.getVehicleId(),
                domain.getDescription(),
                domain.getStatus(),
                domain.getExecutionStartAt().orElse(null),
                domain.getExecutionEndAt().orElse(null),
                WorkOrderLaborItemJpaMapper.toEntitySet(domain.getLaborItems()),
                WorkOrderMaterialItemJpaMapper.toEntitySet(domain.getMaterialItems()),
                WorkOrderBudgetJpaMapper.toEntity(domain.getBudget().orElse(null))
        );
        entity.setDateCreated(domain.getDateCreated());
        entity.setDateUpdated(domain.getDateUpdated());
        entity.setDeletedAt(domain.getDeletedAt());
        return entity;
    }
}