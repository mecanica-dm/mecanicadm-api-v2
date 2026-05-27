package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;

public interface CreateWorkOrderLaborItemUseCase {
    WorkOrderLaborItem handle(CreateWorkOrderLaborItemCommand cmd);
}
