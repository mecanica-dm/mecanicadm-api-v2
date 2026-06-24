package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.StartLaborExecutionCommand;

public class StartLaborExecutionUseCase implements VoidUseCase<StartLaborExecutionCommand> {
    private final WorkOrderGateway gateway;

    public StartLaborExecutionUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(StartLaborExecutionCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.startLaborItem(cmd.laborItemId());

        gateway.update(workOrder);
    }
}
