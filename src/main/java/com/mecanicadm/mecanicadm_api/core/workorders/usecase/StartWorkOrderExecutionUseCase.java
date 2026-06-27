package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.StartWorkOrderExecutionCommand;

public class StartWorkOrderExecutionUseCase {
    private final WorkOrderGateway gateway;

    public StartWorkOrderExecutionUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(StartWorkOrderExecutionCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsInExecution();
        gateway.update(workOrder);
    }
}
