package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.UseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.CreateWorkOrderCommand;

import java.util.UUID;

public class CreateWorkOrderUseCase implements UseCase<CreateWorkOrderCommand, UUID> {
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

