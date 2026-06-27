package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderLaborItemGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveLaborItemFromWorkOrderCommand;

public class RemoveLaborItemFromWorkOrderUseCase {
    private final WorkOrderLaborItemGateway gateway;

    public RemoveLaborItemFromWorkOrderUseCase(WorkOrderLaborItemGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(RemoveLaborItemFromWorkOrderCommand cmd) {
        WorkOrderLaborItem laborItem = gateway.findById(cmd.laborItemId())
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new);

        gateway.delete(laborItem);
    }
}
