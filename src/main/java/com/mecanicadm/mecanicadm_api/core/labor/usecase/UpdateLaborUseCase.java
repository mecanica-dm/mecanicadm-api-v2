package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.exception.LaborExceptions;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;

import java.util.UUID;

public class UpdateLaborUseCase {

    private final LaborGateway gateway;

    public UpdateLaborUseCase(LaborGateway gateway) {
        this.gateway = gateway;
    }

    public UUID execute(UpdateLaborCommand command) {
        Labor labor = gateway.findById(command.id())
                .orElseThrow(LaborExceptions.LaborNotFound::new);

        labor.update(command.name(), command.price());
        return gateway.update(labor).getId();
    }
}
