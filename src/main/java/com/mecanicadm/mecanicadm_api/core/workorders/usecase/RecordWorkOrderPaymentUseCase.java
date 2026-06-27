package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RecordWorkOrderPaymentCommand;

public class RecordWorkOrderPaymentUseCase {
    private final WorkOrderGateway gateway;

    public RecordWorkOrderPaymentUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(RecordWorkOrderPaymentCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsPaid();
        gateway.update(workOrder);
    }
}
