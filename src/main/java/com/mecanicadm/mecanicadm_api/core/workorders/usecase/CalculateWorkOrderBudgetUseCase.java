package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class CalculateWorkOrderBudgetUseCase {
    private final WorkOrderGateway gateway;

    public CalculateWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public UUID execute(CalculateWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        BigDecimal totalMaterials = defaultToZero(gateway.sumMaterialsTotalByWorkOrderId(cmd.workOrderId()));
        BigDecimal totalLabor = defaultToZero(gateway.sumLaborTotalByWorkOrderId(cmd.workOrderId()));
        BigDecimal totalPrice = totalMaterials.add(totalLabor);

        workOrder.getBudget().ifPresentOrElse(
                budget -> budget.updateTotalPrice(totalPrice),
                () -> workOrder.assignBudget(WorkOrderBudget.create(workOrder, totalPrice))
        );

        return gateway.update(workOrder).getId();
    }

    private BigDecimal defaultToZero(BigDecimal value) {
        return Objects.isNull(value) ? BigDecimal.ZERO : value;
    }
}
