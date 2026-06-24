package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.AddMaterialToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.CreateWorkOrderMaterialItemCommand;

public class AddMaterialToWorkOrderUseCase implements VoidUseCase<AddMaterialToWorkOrderCommand> {
    private final WorkOrderGateway gateway;
    private final CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase;

    public AddMaterialToWorkOrderUseCase(WorkOrderGateway gateway,
                                         CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase) {
        this.gateway = gateway;
        this.createWorkOrderMaterialItemUseCase = createWorkOrderMaterialItemUseCase;
    }

    public void execute(AddMaterialToWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderMaterialItem materialItem = createWorkOrderMaterialItemUseCase.execute(
                new CreateWorkOrderMaterialItemCommand(cmd.workOrderId(), cmd.materialId(), cmd.quantity()));

        workOrder.addMaterialItem(materialItem);
        gateway.update(workOrder);
    }
}
