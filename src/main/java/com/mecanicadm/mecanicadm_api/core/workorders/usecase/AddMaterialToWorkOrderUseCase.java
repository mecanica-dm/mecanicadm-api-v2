package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddMaterialToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderMaterialItemCommand;

public class AddMaterialToWorkOrderUseCase {
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
