package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderLaborItemJpaEntity;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class WorkOrderLaborItemJpaMapper {

    public static WorkOrderLaborItem toDomain(WorkOrderLaborItemJpaEntity entity) {
        if (isNull(entity)) return null;
        return WorkOrderLaborItem.restore(
                entity.getId(),
                entity.getLaborId(),
                entity.getExecutionStartAt(),
                entity.getExecutionEndAt(),
                entity.getStatus()
        );
    }

    public static WorkOrderLaborItemJpaEntity toEntity(WorkOrderLaborItem domain) {
        if (isNull(domain)) return null;
        return new WorkOrderLaborItemJpaEntity(
                domain.getId(),
                domain.getLaborId(),
                domain.getExecutionStartAt(),
                domain.getExecutionEndAt(),
                domain.getStatus()
        );
    }

    public static Set<WorkOrderLaborItem> toDomainSet(Set<WorkOrderLaborItemJpaEntity> entities) {
        if (isNull(entities)) return null;
        return entities.stream().map(WorkOrderLaborItemJpaMapper::toDomain).collect(Collectors.toSet());
    }

    public static Set<WorkOrderLaborItemJpaEntity> toEntitySet(Set<WorkOrderLaborItem> domains) {
        if (isNull(domains)) return null;
        return domains.stream().map(WorkOrderLaborItemJpaMapper::toEntity).collect(Collectors.toSet());
    }
}