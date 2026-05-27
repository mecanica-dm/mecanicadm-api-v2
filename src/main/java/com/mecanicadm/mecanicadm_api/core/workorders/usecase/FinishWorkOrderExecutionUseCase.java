package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishWorkOrderExecutionCommand;

public interface FinishWorkOrderExecutionUseCase {
    void handle(FinishWorkOrderExecutionCommand cmd);
}
