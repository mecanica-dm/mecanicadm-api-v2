package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.usecase.command.SoftDeleteMaterialCommand;

public interface SoftDeleteMaterialUseCase {
    void handle(SoftDeleteMaterialCommand cmd);
}
