package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.projections.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderPageQuery;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderPageResult;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.mapper.WorkOrderJpaMapper;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.specification.WorkOrderSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkOrderRepositoryImpl implements WorkOrderGateway {

    private final WorkOrderJpaRepository jpaRepository;

    public WorkOrderRepositoryImpl(WorkOrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public WorkOrder create(WorkOrder workOrder) {
        WorkOrderJpaEntity entity = WorkOrderJpaMapper.toEntity(workOrder);
        WorkOrderJpaEntity saved = jpaRepository.save(entity);
        return WorkOrderJpaMapper.toDomain(saved);
    }

    @Override
    public WorkOrder update(WorkOrder workOrder) {
        WorkOrderJpaEntity entity = WorkOrderJpaMapper.toEntity(workOrder);
        WorkOrderJpaEntity saved = jpaRepository.save(entity);
        return WorkOrderJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<WorkOrder> findById(UUID id) {
        return jpaRepository.findById(id).map(WorkOrderJpaMapper::toDomain);
    }

    @Override
    public Optional<WorkOrder> findByIdWithItems(UUID id) {
        return jpaRepository.findByIdWithItems(id).map(WorkOrderJpaMapper::toDomain);
    }

    @Override
    public WorkOrderPageResult findAll(WorkOrderPageQuery query) {
        Specification<WorkOrderJpaEntity> spec = WorkOrderSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        var page = jpaRepository.findAll(spec, pageable);
        return new WorkOrderPageResult(page.map(WorkOrderJpaMapper::toDomain).getContent(),
                page.getTotalElements()
        );
    }

    @Override
    public BigDecimal sumMaterialsTotalByWorkOrderId(UUID workOrderId) {
        return jpaRepository.sumMaterialsTotalByWorkOrderId(workOrderId);

    }

    @Override
    public BigDecimal sumLaborTotalByWorkOrderId(UUID workOrderId) {
        return jpaRepository.sumLaborTotalByWorkOrderId(workOrderId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);

    }

    @Override
    public WorkOrderExecutionSummaryProjection getExecutionTimeSummary(LocalDateTime initialDateTime, LocalDateTime finalDateTimeExclusive) {
        return jpaRepository.getExecutionTimeSummary(initialDateTime, finalDateTimeExclusive);
    }

    @Override
    public WorkOrderExecutionDurationProjection getSlowestExecution(LocalDateTime initialDateTime, LocalDateTime finalDateTimeExclusive) {
        return jpaRepository.getSlowestExecution(initialDateTime, finalDateTimeExclusive);
    }

    @Override
    public WorkOrderExecutionDurationProjection getFastestExecution(LocalDateTime initialDateTime, LocalDateTime finalDateTimeExclusive) {
        return jpaRepository.getFastestExecution(initialDateTime, finalDateTimeExclusive);
    }

    @Override
    public Double getAverageLaborExecutionMinutes(LocalDateTime initialDateTime, LocalDateTime finalDateTimeExclusive) {
        return jpaRepository.getAverageLaborExecutionMinutes(initialDateTime, finalDateTimeExclusive);
    }
}
