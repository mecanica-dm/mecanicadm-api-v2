package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.usecase.command.UpdateMaterialCommand;

public interface UpdateMaterialUseCase {
    void handle(UpdateMaterialCommand cmd);
}
