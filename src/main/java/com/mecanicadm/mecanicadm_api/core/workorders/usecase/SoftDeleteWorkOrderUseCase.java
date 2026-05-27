package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.SoftDeleteWorkOrderCommand;

public interface SoftDeleteWorkOrderUseCase {
    void handle(SoftDeleteWorkOrderCommand cmd);
}
