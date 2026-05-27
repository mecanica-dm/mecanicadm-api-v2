package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public record CalculateWorkOrderBudgetCommand(
        @JsonIgnore
        UUID workOrderId
) {
}
