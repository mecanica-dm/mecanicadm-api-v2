package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishWorkOrderExecutionCommand;

public class FinishWorkOrderExecutionUseCase {
    private final WorkOrderGateway gateway;

    public FinishWorkOrderExecutionUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(FinishWorkOrderExecutionCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsExecutionCompleted();
        gateway.update(workOrder);
    }
}
