package com.mecanicadm.mecanicadm_api.infra.features.workorder.api.dto.response;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;

import java.math.BigDecimal;

public record WorkOrderBudgetResponse(
        BigDecimal totalPrice,
        WorkOrderBudgetStatus status,
        String rejectionReason
) {
    public WorkOrderBudgetResponse(WorkOrderBudget budget) {
        this(
                budget.getTotalPrice(),
                budget.getStatus(),
                budget.getRejectionReason()
        );
    }
}
