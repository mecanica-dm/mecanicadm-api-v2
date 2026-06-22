package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.domain.Material;
import com.mecanicadm.mecanicadm_api.core.material.domain.port.MaterialGateway;
import com.mecanicadm.mecanicadm_api.core.material.exception.MaterialExceptions;
import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;
import com.mecanicadm.mecanicadm_api.shared.usecase.VoidUseCase;

public class SoftDeleteMaterialUseCase implements VoidUseCase<SoftDeleteMaterialCommand> {

    private final MaterialGateway gateway;

    public SoftDeleteMaterialUseCase(MaterialGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(SoftDeleteMaterialCommand cmd) {
        Material material = gateway.findById(cmd.id())
                .orElseThrow(MaterialExceptions.MaterialNotFound::new);
        material.delete();
        gateway.update(material);
    }
}
