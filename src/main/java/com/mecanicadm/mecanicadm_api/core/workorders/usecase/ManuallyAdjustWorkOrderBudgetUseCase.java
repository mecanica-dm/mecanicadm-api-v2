package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.ManuallyAdjustWorkOrderBudgetCommand;

public class ManuallyAdjustWorkOrderBudgetUseCase {
    private final WorkOrderGateway gateway;

    public ManuallyAdjustWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(ManuallyAdjustWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getBudget()
                .orElseThrow(WorkOrderExceptions.BudgetNotFound::new)
                .updateTotalPrice(cmd.newTotalPrice());

        gateway.update(workOrder);
    }
}
