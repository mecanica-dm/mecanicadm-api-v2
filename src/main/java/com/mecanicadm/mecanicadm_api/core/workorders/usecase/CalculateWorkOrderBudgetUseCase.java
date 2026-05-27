package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CalculateWorkOrderBudgetCommand;

import java.util.UUID;

public interface CalculateWorkOrderBudgetUseCase {
    UUID handle(CalculateWorkOrderBudgetCommand cmd);
}
