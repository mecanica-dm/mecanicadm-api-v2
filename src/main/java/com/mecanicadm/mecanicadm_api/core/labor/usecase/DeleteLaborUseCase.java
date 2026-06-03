package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.DeleteLaborCommand;

public class DeleteLaborUseCase {

    private final LaborGateway gateway;

    public DeleteLaborUseCase(LaborGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(DeleteLaborCommand command) {
        if (!gateway.existsById(command.id())) {
            throw new LaborExceptions.LaborNotFound();
        }
        gateway.deleteById(command.id());
    }
}
