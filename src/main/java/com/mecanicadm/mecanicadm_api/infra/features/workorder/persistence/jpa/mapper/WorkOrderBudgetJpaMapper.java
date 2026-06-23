package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.mapper;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderBudgetJpaEntity;

import static java.util.Objects.isNull;

public class WorkOrderBudgetJpaMapper {

    public static WorkOrderBudget toDomain(WorkOrderBudgetJpaEntity entity) {
        if (isNull(entity)) return null;
        return new WorkOrderBudget(
                entity.getWorkOrderId(),
                entity.getTotalPrice(),
                entity.getStatus(),
                entity.getRejectionReason()
        );
    }

    public static WorkOrderBudgetJpaEntity toEntity(WorkOrderBudget domain) {
        if (isNull(domain)) return null;
        return new WorkOrderBudgetJpaEntity(
                domain.getWorkOrderId(),
                domain.getTotalPrice(),
                domain.getStatus(),
                domain.getRejectionReason()
        );
    }
}
