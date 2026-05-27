package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.ManuallyAdjustWorkOrderBudgetUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.ManuallyAdjustWorkOrderBudgetCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManuallyAdjustWorkOrderBudgetService implements ManuallyAdjustWorkOrderBudgetUseCase {

    private final WorkOrderRepository workOrderRepository;

    public ManuallyAdjustWorkOrderBudgetService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(ManuallyAdjustWorkOrderBudgetCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.getBudget()
                .orElseThrow(WorkOrderExceptions.BudgetNotFound::new)
                .updateTotalPrice(cmd.newTotalPrice());

        workOrderRepository.save(workOrder);
    }
}
