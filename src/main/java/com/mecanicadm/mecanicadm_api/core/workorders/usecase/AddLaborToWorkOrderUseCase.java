package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddLaborToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;

public class AddLaborToWorkOrderUseCase {

    private final WorkOrderGateway gateway;
    private final CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase;

    public AddLaborToWorkOrderUseCase(WorkOrderGateway gateway, CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase) {
        this.gateway = gateway;
        this.createWorkOrderLaborItemUseCase = createWorkOrderLaborItemUseCase;
    }

    public void execute(AddLaborToWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderLaborItem laborItem = createWorkOrderLaborItemUseCase.execute(new CreateWorkOrderLaborItemCommand(cmd.laborId()));
        workOrder.addLaborItem(laborItem);

        gateway.update(workOrder);
    }
}
