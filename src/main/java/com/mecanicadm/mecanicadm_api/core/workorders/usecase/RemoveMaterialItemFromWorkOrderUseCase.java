package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.RemoveMaterialItemFromWorkOrderCommand;

public interface RemoveMaterialItemFromWorkOrderUseCase {
    void handle(RemoveMaterialItemFromWorkOrderCommand cmd);
}
