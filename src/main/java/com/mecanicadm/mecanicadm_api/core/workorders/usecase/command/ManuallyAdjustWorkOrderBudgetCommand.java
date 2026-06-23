package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import java.math.BigDecimal;
import java.util.UUID;

public record ManuallyAdjustWorkOrderBudgetCommand(
        UUID workOrderId,
        BigDecimal newTotalPrice
) {
    public ManuallyAdjustWorkOrderBudgetCommand withId(UUID id) {
        return new ManuallyAdjustWorkOrderBudgetCommand(id, this.newTotalPrice);
    }
}
