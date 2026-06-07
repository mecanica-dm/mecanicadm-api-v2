package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.UpdateMaterialCommand;

public class UpdateMaterialUseCase {

    private final MaterialGateway gateway;

    public UpdateMaterialUseCase(MaterialGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(UpdateMaterialCommand cmd) {
        Material material = gateway.findById(cmd.id())
                .orElseThrow(MaterialExceptions.MaterialNotFound::new);

        material.update(
                cmd.name(),
                cmd.brand(),
                cmd.description(),
                cmd.price(),
                cmd.type()
        );

        gateway.update(material);
    }
}
