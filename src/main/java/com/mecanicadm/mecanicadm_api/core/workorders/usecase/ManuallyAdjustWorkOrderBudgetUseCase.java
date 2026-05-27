package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.ManuallyAdjustWorkOrderBudgetCommand;

public interface ManuallyAdjustWorkOrderBudgetUseCase {
    void handle(ManuallyAdjustWorkOrderBudgetCommand cmd);
}
