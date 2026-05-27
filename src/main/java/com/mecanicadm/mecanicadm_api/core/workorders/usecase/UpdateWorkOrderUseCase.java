package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.UpdateWorkOrderCommand;

public interface UpdateWorkOrderUseCase {
    void handle(UpdateWorkOrderCommand cmd);
}
