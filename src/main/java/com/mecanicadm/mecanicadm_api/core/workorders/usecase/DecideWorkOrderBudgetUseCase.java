package com.mecanicadm.mecanicadm_api.core.workorders.usecase;

import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.port.WorkOrderGateway;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DecideWorkOrderBudgetCommand;
import org.springframework.util.StringUtils;

public class DecideWorkOrderBudgetUseCase {
    private final WorkOrderGateway gateway;

    public DecideWorkOrderBudgetUseCase(WorkOrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(DecideWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = gateway.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderBudget budget = workOrder.getBudget()
                .orElseThrow(WorkOrderExceptions.BudgetNotFound::new);

        if (budget.getStatus() != WorkOrderBudgetStatus.WAITING_DECISION) {
            throw new WorkOrderExceptions.BudgetNotWaitingDecision();
        }

        processDecision(budget, cmd);
        gateway.create(workOrder);
    }

    private void processDecision(WorkOrderBudget budget, DecideWorkOrderBudgetCommand cmd) {
        switch (cmd.decision()) {
            case APPROVED -> budget.approve();
            case REJECTED -> {
                validateRejectionReason(cmd.rejectionReason());
                budget.reject(cmd.rejectionReason(), false);
            }
            case CHANGES_REQUESTED -> {
                validateRejectionReason(cmd.rejectionReason());
                budget.reject(cmd.rejectionReason(), true);
            }
            default -> throw new WorkOrderExceptions.BudgetDecisionInvalid(cmd.decision().name());
        }
    }

    private void validateRejectionReason(String reason) {
        if (!StringUtils.hasText(reason)) {
            throw new WorkOrderExceptions.BudgetRejectionReasonRequired();
        }
    }
}
