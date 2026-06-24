package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;

import java.util.UUID;

public record DecideWorkOrderBudgetCommand(
        UUID workOrderId,
        WorkOrderBudgetStatus decision,
        String rejectionReason
) {
    public DecideWorkOrderBudgetCommand withId(UUID workOrderId) {
        return new DecideWorkOrderBudgetCommand(workOrderId, decision, rejectionReason);
    }
}
