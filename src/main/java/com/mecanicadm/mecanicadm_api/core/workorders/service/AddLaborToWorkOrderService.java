package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderLaborItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.AddLaborToWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderLaborItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddLaborToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderLaborItemCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddLaborToWorkOrderService implements AddLaborToWorkOrderUseCase {

    private final WorkOrderRepository workOrderRepository;
    private final CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase;

    public AddLaborToWorkOrderService(WorkOrderRepository workOrderRepository, CreateWorkOrderLaborItemUseCase createWorkOrderLaborItemUseCase) {
        this.workOrderRepository = workOrderRepository;
        this.createWorkOrderLaborItemUseCase = createWorkOrderLaborItemUseCase;
    }

    @Transactional
    public void handle(AddLaborToWorkOrderCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderLaborItem laborItem = createWorkOrderLaborItemUseCase.handle(new CreateWorkOrderLaborItemCommand(cmd.laborId()));
        workOrder.addLaborItem(laborItem);

        workOrderRepository.save(workOrder);
    }
}
