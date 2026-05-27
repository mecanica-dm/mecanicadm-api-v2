package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderCommand;

import java.util.UUID;

public interface CreateWorkOrderUseCase {
    UUID handle(CreateWorkOrderCommand cmd);
}

