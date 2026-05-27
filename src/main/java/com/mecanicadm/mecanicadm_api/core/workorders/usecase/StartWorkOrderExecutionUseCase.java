package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.StartWorkOrderExecutionCommand;

public interface StartWorkOrderExecutionUseCase {
    void handle(StartWorkOrderExecutionCommand cmd);
}
