package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderLaborItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateWorkOrderLaborItemService implements CreateWorkOrderLaborItemUseCase {

    private final LaborGateway laborGateway;

    public CreateWorkOrderLaborItemService(LaborGateway laborGateway) {
        this.laborGateway = laborGateway;
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderLaborItem handle(CreateWorkOrderLaborItemCommand cmd) {
        if (!laborGateway.existsById(cmd.laborId())) {
            throw new LaborExceptions.LaborNotFound();
        }

        return WorkOrderLaborItem.create(cmd.laborId());
    }
}
