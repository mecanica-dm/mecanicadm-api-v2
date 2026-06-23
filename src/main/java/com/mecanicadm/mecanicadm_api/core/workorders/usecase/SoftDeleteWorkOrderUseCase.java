package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SoftDeleteWorkOrderCommand;

public class SoftDeleteWorkOrderUseCase {
    private final WorkOrderGateway gateway;
    private final RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase;

    public SoftDeleteWorkOrderUseCase(WorkOrderGateway gateway,
                                      RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase) {
        this.gateway = gateway;
        this.removeMaterialItemFromWorkOrderUseCase = removeMaterialItemFromWorkOrderUseCase;
    }

    public void execute(SoftDeleteWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.id()).orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getMaterialItems().forEach(item -> removeMaterialItemFromWorkOrderUseCase.handle(
                new RemoveMaterialItemFromWorkOrderCommand(workOrder.getId(), item.getMaterialId())));

        gateway.deleteById(cmd.id());
    }
}
