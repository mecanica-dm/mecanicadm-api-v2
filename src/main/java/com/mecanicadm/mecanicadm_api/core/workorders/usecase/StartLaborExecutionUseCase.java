package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.StartLaborExecutionCommand;

public interface StartLaborExecutionUseCase {
    void handle(StartLaborExecutionCommand cmd);
}
