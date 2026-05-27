package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.FinishLaborExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishLaborExecutionCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinishLaborExecutionService implements FinishLaborExecutionUseCase {

    private final WorkOrderRepository workOrderRepository;

    public FinishLaborExecutionService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(FinishLaborExecutionCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.findLaborItem(cmd.laborItemId())
                .orElseThrow(WorkOrderExceptions.LaborItemNotFound::new)
                .finishExecution();

        workOrderRepository.save(workOrder);
    }
}
