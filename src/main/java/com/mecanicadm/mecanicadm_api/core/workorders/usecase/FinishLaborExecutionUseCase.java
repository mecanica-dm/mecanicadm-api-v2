package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishLaborExecutionCommand;

public interface FinishLaborExecutionUseCase {
    void handle(FinishLaborExecutionCommand cmd);
}
