package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.DeleteLaborCommand;

public interface DeleteLaborUseCase {
    void handle(DeleteLaborCommand cmd);
}
