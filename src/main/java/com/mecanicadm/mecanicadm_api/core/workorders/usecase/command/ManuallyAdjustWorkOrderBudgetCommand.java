package com.mecanicadm.mecanicadm_api.core.workorders.usecase.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record ManuallyAdjustWorkOrderBudgetCommand(
        @JsonIgnore
        UUID workOrderId,

        @NotNull(message = "{validation.workorder.budget.price.required}")
        @PositiveOrZero(message = "{validation.workorder.budget.price.positiveorzero}")
        BigDecimal newTotalPrice
) {
    public ManuallyAdjustWorkOrderBudgetCommand withId(UUID id) {
        return new ManuallyAdjustWorkOrderBudgetCommand(id, this.newTotalPrice);
    }
}
