package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.StartLaborExecutionUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.StartLaborExecutionCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StartLaborExecutionService implements StartLaborExecutionUseCase {

    private final WorkOrderRepository workOrderRepository;

    public StartLaborExecutionService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void handle(StartLaborExecutionCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        workOrder.startLaborItem(cmd.laborItemId());

        workOrderRepository.save(workOrder);
    }
}
