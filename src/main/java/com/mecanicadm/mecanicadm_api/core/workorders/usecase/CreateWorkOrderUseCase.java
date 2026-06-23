package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;

import java.util.UUID;

public class CreateWorkOrderUseCase {
    private final WorkOrderGateway gateway;

    public CreateWorkOrderUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public UUID execute(CreateWorkOrderCommand cmd) {
        WorkOrder workOrder = WorkOrder.create(cmd.clientId(), cmd.vehicleId(), cmd.description());
        gateway.create(workOrder);
        return workOrder.getId();
    }
}

