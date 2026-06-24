package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.RemoveMaterialItemFromWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.SoftDeleteWorkOrderCommand;

public class SoftDeleteWorkOrderUseCase implements VoidUseCase<SoftDeleteWorkOrderCommand> {
    private final WorkOrderGateway gateway;
    private final RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase;

    public SoftDeleteWorkOrderUseCase(WorkOrderGateway gateway,
                                      RemoveMaterialItemFromWorkOrderUseCase removeMaterialItemFromWorkOrderUseCase) {
        this.gateway = gateway;
        this.removeMaterialItemFromWorkOrderUseCase = removeMaterialItemFromWorkOrderUseCase;
    }

    public void execute(SoftDeleteWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.id()).orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getMaterialItems().forEach(item -> removeMaterialItemFromWorkOrderUseCase.execute(
                new RemoveMaterialItemFromWorkOrderCommand(workOrder.getId(), item.getMaterialId())));

        workOrder.delete();
        gateway.update(workOrder);
    }
}
