package com.mecanicadm.mecanicadm_api.core.workorder.usecase;

import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorder.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorder.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.AddLaborToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorder.usecase.command.CreateWorkOrderLaborItemCommand;
import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

public class AddLaborToWorkOrderUseCase implements VoidUseCase<AddLaborToWorkOrderCommand> {

    private final WorkOrderGateway gateway;
    private final CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase;

    public AddLaborToWorkOrderUseCase(WorkOrderGateway gateway, CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase) {
        this.gateway = gateway;
        this.createWorkOrderLaborItemUseCase = createWorkOrderLaborItemUseCase;
    }

    @Override
    public void execute(AddLaborToWorkOrderCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderLaborItem laborItem = createWorkOrderLaborItemUseCase.execute(new CreateWorkOrderLaborItemCommand(cmd.laborId()));
        workOrder.addLaborItem(laborItem);

        gateway.update(workOrder);
    }
}
