package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import java.util.UUID;

public record CalculateWorkOrderBudgetCommand(
        UUID workOrderId
) {
}
