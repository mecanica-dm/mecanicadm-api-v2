package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.StartWorkOrderExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.StartWorkOrderExecutionCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StartWorkOrderExecutionService implements StartWorkOrderExecutionUseCase {

    private final WorkOrderRepository workOrderRepository;

    public StartWorkOrderExecutionService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(StartWorkOrderExecutionCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.markAsInExecution();
        workOrderRepository.save(workOrder);
    }
}
