package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderMaterialItemCommand;

public interface CreateWorkOrderMaterialItemUseCase {
    WorkOrderMaterialItem handle(CreateWorkOrderMaterialItemCommand cmd);
}
