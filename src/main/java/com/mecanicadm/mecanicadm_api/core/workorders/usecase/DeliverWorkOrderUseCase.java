package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DeliverWorkOrderCommand;

public class DeliverWorkOrderUseCase {
    private final WorkOrderGateway gateway;

    public DeliverWorkOrderUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(DeliverWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsDelivered();
        gateway.update(workOrder);
    }
}
