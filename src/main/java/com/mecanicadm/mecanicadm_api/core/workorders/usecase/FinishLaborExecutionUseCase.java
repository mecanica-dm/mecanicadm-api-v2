package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishLaborExecutionCommand;

public class FinishLaborExecutionUseCase {
    private final WorkOrderGateway gateway;

    public FinishLaborExecutionUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(FinishLaborExecutionCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.findLaborItem(cmd.laborItemId())
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new)
                .finishExecution();

        gateway.update(workOrder);
    }
}
