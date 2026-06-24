package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderLaborItemGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.RemoveLaborItemFromWorkOrderCommand;

public class RemoveLaborItemFromWorkOrderUseCase implements VoidUseCase<RemoveLaborItemFromWorkOrderCommand> {
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
