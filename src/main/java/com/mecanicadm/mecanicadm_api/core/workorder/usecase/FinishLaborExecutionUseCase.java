package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.FinishLaborExecutionCommand;

public class FinishLaborExecutionUseCase implements VoidUseCase<FinishLaborExecutionCommand> {
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
