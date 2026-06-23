package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;

public class CreateWorkOrderLaborItemUseCase {

    private final LaborGateway laborGateway;

    public CreateWorkOrderLaborItemUseCase(LaborGateway laborGateway) {
        this.laborGateway = laborGateway;
    }

    public WorkOrderLaborItem execute(CreateWorkOrderLaborItemCommand cmd) {
        if (!laborGateway.existsById(cmd.laborId())) {
            throw new LaborExceptions.LaborNotFound();
        }

        return WorkOrderLaborItem.create(cmd.laborId());
    }
}
