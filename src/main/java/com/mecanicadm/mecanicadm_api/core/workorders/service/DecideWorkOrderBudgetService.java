package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderBudget;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.enums.WorkOrderBudgetStatus;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.DecideWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.DecideWorkOrderBudgetCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class DecideWorkOrderBudgetService implements DecideWorkOrderBudgetUseCase {

    private final WorkOrderRepository workOrderRepository;

    public DecideWorkOrderBudgetService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(DecideWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderBudget budget = workOrder.getBudget()
                .orElseThrow(WorkOrderExceptions.BudgetNotFound::new);

        if (budget.getStatus() != WorkOrderBudgetStatus.WAITING_DECISION) {
            throw new WorkOrderExceptions.BudgetNotWaitingDecision();
        }

        processDecision(budget, cmd);
        workOrderRepository.save(workOrder);
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
