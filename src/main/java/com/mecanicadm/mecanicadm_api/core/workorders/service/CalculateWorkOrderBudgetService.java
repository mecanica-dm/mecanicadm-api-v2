package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CalculateWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class CalculateWorkOrderBudgetService implements CalculateWorkOrderBudgetUseCase {

    private final WorkOrderRepository workOrderRepository;

    public CalculateWorkOrderBudgetService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public UUID handle(CalculateWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        BigDecimal totalMaterials = defaultToZero(workOrderRepository.sumMaterialsTotalByWorkOrderId(cmd.workOrderId()));
        BigDecimal totalLabor = defaultToZero(workOrderRepository.sumLaborTotalByWorkOrderId(cmd.workOrderId()));
        BigDecimal totalPrice = totalMaterials.add(totalLabor);

        workOrder.getBudget().ifPresentOrElse(
                budget -> budget.updateTotalPrice(totalPrice),
                () -> workOrder.assignBudget(WorkOrderBudget.create(workOrder, totalPrice))
        );

        return workOrderRepository.save(workOrder).getId();
    }

    private BigDecimal defaultToZero(BigDecimal value) {
        return Objects.isNull(value) ? BigDecimal.ZERO : value;
    }
}
