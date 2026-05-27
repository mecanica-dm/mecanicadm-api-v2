package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.FinishWorkOrderExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.FinishWorkOrderExecutionCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinishWorkOrderExecutionService implements FinishWorkOrderExecutionUseCase {

    private final WorkOrderRepository workOrderRepository;

    public FinishWorkOrderExecutionService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(FinishWorkOrderExecutionCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsExecutionCompleted();
        workOrderRepository.save(workOrder);
    }
}
