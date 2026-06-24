package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderLaborItemGateway;
import com.mecanicadm.mecanicadm_api.shared.exception.TechnicalException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Repository
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
        if (isNull(laborItem)) {
            throw new TechnicalException("error.technical.entity.null", "WorkOrderLaborItem", "exclusão");
        }
        jpaRepository.delete(WorkOrderLaborItemJpaMapper.toEntity(laborItem));
    }
}
