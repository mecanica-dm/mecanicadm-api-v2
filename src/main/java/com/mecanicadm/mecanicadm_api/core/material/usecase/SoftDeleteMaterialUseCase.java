package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;

public class SoftDeleteMaterialUseCase {

    private final MaterialGateway gateway;

    public SoftDeleteMaterialUseCase(MaterialGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(SoftDeleteMaterialCommand cmd) {
        if (!gateway.existsById(cmd.id())) {
            throw new MaterialExceptions.MaterialNotFound();
        }
        gateway.deleteById(cmd.id());
    }
}
