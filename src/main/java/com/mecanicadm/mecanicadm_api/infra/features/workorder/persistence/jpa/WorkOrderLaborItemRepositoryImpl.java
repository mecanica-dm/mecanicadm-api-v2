package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderLaborItemGateway;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.mapper.WorkOrderLaborItemJpaMapper;

import java.util.Optional;
import java.util.UUID;

public class WorkOrderLaborItemRepositoryImpl implements WorkOrderLaborItemGateway {

    private final WorkOrderLaborItemJpaRepository jpaRepository;

    public WorkOrderLaborItemRepositoryImpl(WorkOrderLaborItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<WorkOrderLaborItem> findById(UUID id) {
        return jpaRepository.findById(id).map(WorkOrderLaborItemJpaMapper::toDomain);
    }

    @Override
    public void delete(WorkOrderLaborItem laborItem) {
        jpaRepository.delete(WorkOrderLaborItemJpaMapper.toEntity(laborItem));
    }
}
