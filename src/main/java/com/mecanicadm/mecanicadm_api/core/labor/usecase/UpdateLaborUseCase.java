package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.UpdateLaborCommand;

import java.util.UUID;

public interface UpdateLaborUseCase {
    UUID handle(UpdateLaborCommand cmd);
}
