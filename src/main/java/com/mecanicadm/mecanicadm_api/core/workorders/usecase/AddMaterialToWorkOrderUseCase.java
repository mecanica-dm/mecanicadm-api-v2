package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddMaterialToWorkOrderCommand;

public interface AddMaterialToWorkOrderUseCase {
    void handle(AddMaterialToWorkOrderCommand cmd);
}
