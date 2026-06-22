package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DecideWorkOrderBudgetCommand(
        @JsonIgnore
        UUID workOrderId,

        @NotNull(message = "{validation.workorder.budget.decision.required}")
        WorkOrderBudgetStatus decision,

        String rejectionReason
) {
    public DecideWorkOrderBudgetCommand withId(UUID workOrderId) {
        return new DecideWorkOrderBudgetCommand(workOrderId, decision, rejectionReason);
    }
}
