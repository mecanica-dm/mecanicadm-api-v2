package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveLaborItemFromWorkOrderCommand;

public interface RemoveLaborItemFromWorkOrderUseCase {
    void handle(RemoveLaborItemFromWorkOrderCommand cmd);
}
