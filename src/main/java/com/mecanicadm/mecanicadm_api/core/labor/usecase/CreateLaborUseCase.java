package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.domain.Labor;
import com.mecanicadm.mecanicadm_api.core.labor.domain.port.LaborGateway;
import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;

import java.util.UUID;

public class CreateLaborUseCase {

    private final LaborGateway gateway;

    public CreateLaborUseCase(LaborGateway gateway) {
        this.gateway = gateway;
    }

    public UUID execute(CreateLaborCommand command) {
        Labor created = gateway.create(Labor.create(command.name(), command.price()));
        return created.getId();
    }
}
