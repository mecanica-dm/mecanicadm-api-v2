package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DiagnoseWorkOrderCommand;

import java.util.UUID;

public interface DiagnoseWorkOrderUseCase {
    UUID handle(DiagnoseWorkOrderCommand cmd);
}

