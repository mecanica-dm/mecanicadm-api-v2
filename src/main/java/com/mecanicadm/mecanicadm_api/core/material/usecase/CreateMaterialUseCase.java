package com.mecanicadm.mecanicadm_api.core.material.usecase;

import com.mecanicadm.mecanicadm_api.core.material.usecase.command.CreateMaterialCommand;

import java.util.UUID;

public interface CreateMaterialUseCase {
    UUID handle(CreateMaterialCommand cmd);
}
