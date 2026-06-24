package com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderExecutionDurationProjection;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderExecutionSummaryProjection;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderPageQuery;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderPageResult;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.entity.WorkOrderJpaEntity;
import com.mecanicadm.mecanicadm_api.infra.features.workorder.persistence.jpa.specification.WorkOrderSpecificationBuilder;
import com.mecanicadm.mecanicadm_api.shared.exception.TechnicalException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Repository
public class WorkOrderRepositoryImpl implements WorkOrderGateway {

    private final WorkOrderJpaRepository jpaRepository;
    private final WorkOrderLaborItemJpaRepository laborItemJpaRepository;
    private final WorkOrderMaterialItemJpaRepository materialItemJpaRepository;
    private final WorkOrderBudgetJpaRepository budgetJpaRepository;

    public WorkOrderRepositoryImpl(WorkOrderJpaRepository jpaRepository,
                                   WorkOrderLaborItemJpaRepository laborItemJpaRepository,
                                   WorkOrderMaterialItemJpaRepository materialItemJpaRepository,
                                   WorkOrderBudgetJpaRepository budgetJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.laborItemJpaRepository = laborItemJpaRepository;
        this.materialItemJpaRepository = materialItemJpaRepository;
        this.budgetJpaRepository = budgetJpaRepository;
    }

    @Override
    public WorkOrder create(WorkOrder workOrder) {
        if (isNull(workOrder)) {
            throw new TechnicalException("error.technical.entity.null", "WorkOrder", "criação");
        }
        WorkOrderJpaEntity entity = WorkOrderJpaMapper.toEntity(workOrder);
        entity = jpaRepository.save(entity);

        saveChildren(workOrder, entity.getId());

        return findByIdWithItems(entity.getId()).orElse(null);
    }

    @Override
    public WorkOrder update(WorkOrder workOrder) {
        if (isNull(workOrder)) {
            throw new TechnicalException("error.technical.entity.null", "WorkOrder", "atualização");
        }
        laborItemJpaRepository.deleteByWorkOrderId(workOrder.getId());
        materialItemJpaRepository.deleteByWorkOrderId(workOrder.getId());
        budgetJpaRepository.deleteById(workOrder.getId());

        WorkOrderJpaEntity entity = WorkOrderJpaMapper.toEntity(workOrder);
        entity = jpaRepository.save(entity);

        saveChildren(workOrder, entity.getId());

        return findByIdWithItems(entity.getId()).orElse(null);
    }

    private void saveChildren(WorkOrder workOrder, UUID workOrderId) {
        var laborItemEntities = WorkOrderLaborItemJpaMapper.toEntitySet(workOrder.getLaborItems());
        laborItemEntities.forEach(item -> item.setWorkOrderId(workOrderId));
        laborItemJpaRepository.saveAll(laborItemEntities);

        var materialItemEntities = WorkOrderMaterialItemJpaMapper.toEntitySet(workOrder.getMaterialItems());
        materialItemEntities.forEach(item -> item.setWorkOrderId(workOrderId));
        materialItemJpaRepository.saveAll(materialItemEntities);

        workOrder.getBudget().ifPresent(budget -> {
            var budgetEntity = WorkOrderBudgetJpaMapper.toEntity(budget);
            budgetEntity.setWorkOrderId(workOrderId);
            budgetJpaRepository.save(budgetEntity);
        });
    }

    @Override
    public Optional<WorkOrder> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainWithItems);
    }

    @Override
    public Optional<WorkOrder> findByIdWithItems(UUID id) {
        return jpaRepository.findById(id).map(this::toDomainWithItems);
    }

    private WorkOrder toDomainWithItems(WorkOrderJpaEntity entity) {
        var laborItems = WorkOrderLaborItemJpaMapper.toDomainSet(
                laborItemJpaRepository.findByWorkOrderId(entity.getId()));
        var materialItems = WorkOrderMaterialItemJpaMapper.toDomainSet(
                materialItemJpaRepository.findByWorkOrderId(entity.getId()));
        var budget = budgetJpaRepository.findById(entity.getId())
                .map(WorkOrderBudgetJpaMapper::toDomain).orElse(null);
        return WorkOrderJpaMapper.toDomain(entity, laborItems, materialItems, budget);
    }

    @Override
    public WorkOrderPageResult findAll(WorkOrderPageQuery query) {
        Specification<WorkOrderJpaEntity> spec = WorkOrderSpecificationBuilder.buildFilterSpecification(query.filter());
        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sortBy());
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        var page = jpaRepository.findAll(spec, pageable);
        return new WorkOrderPageResult(page.map(WorkOrderJpaMapper::toDomainLight).getContent(),
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
