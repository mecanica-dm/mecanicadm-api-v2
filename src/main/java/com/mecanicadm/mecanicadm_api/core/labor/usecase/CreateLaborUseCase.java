package com.mecanicadm.mecanicadm_api.core.labor.usecase;

import com.mecanicadm.mecanicadm_api.core.labor.usecase.command.CreateLaborCommand;

import java.util.UUID;

public interface CreateLaborUseCase {
    UUID handle(CreateLaborCommand cmd);
}
