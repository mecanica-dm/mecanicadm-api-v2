package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.SoftDeleteStockUseCase;
import com.mecanicadm.mecanicadm_api.core.stockmovements.usecase.command.SoftDeleteStockCommand;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.RemoveMaterialItemFromWorkOrderCommand;

public class RemoveMaterialItemFromWorkOrderUseCase implements VoidUseCase<RemoveMaterialItemFromWorkOrderCommand> {
    private final WorkOrderGateway gateway;
    private final SoftDeleteStockUseCase softDeleteStockUseCase;

    public RemoveMaterialItemFromWorkOrderUseCase(WorkOrderGateway gateway,
                                                  SoftDeleteStockUseCase softDeleteStockUseCase) {
        this.gateway = gateway;
        this.softDeleteStockUseCase = softDeleteStockUseCase;
    }

    public void execute(RemoveMaterialItemFromWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        softDeleteStockUseCase.execute(new SoftDeleteStockCommand(cmd.materialId(), cmd.workOrderId()));

        workOrder.removeMaterialItem(cmd.materialId());
        gateway.update(workOrder);
    }
}
