package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;

public class UpdateWorkOrderUseCase {
    private final WorkOrderGateway gateway;

    public UpdateWorkOrderUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(UpdateWorkOrderCommand cmd) {
        var workOrder = gateway.findById(cmd.id())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.update(cmd.clientId(), cmd.vehicleId(), cmd.description());

        gateway.update(workOrder);
    }
}
