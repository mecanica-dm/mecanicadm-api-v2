package com.mecanicadm.mecanicadm_api.core.workorders.service;

import com.mecanicadm.mecanicadm_api.core.workorders.adapter.repository.WorkOrderRepository;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrder;
import com.mecanicadm.mecanicadm_api.core.workorders.domain.WorkOrderMaterialItem;
import com.mecanicadm.mecanicadm_api.core.workorders.exception.WorkOrderExceptions;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.AddMaterialToWorkOrderUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.CreateWorkOrderMaterialItemUseCase;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.AddMaterialToWorkOrderCommand;
import com.mecanicadm.mecanicadm_api.core.workorders.usecase.command.CreateWorkOrderMaterialItemCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddMaterialToWorkOrderService implements AddMaterialToWorkOrderUseCase {

    private final WorkOrderRepository workOrderRepository;
    private final CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase;

    public AddMaterialToWorkOrderService(WorkOrderRepository workOrderRepository,
                                         CreateWorkOrderMaterialItemUseCase createWorkOrderMaterialItemUseCase) {
        this.workOrderRepository = workOrderRepository;
        this.createWorkOrderMaterialItemUseCase = createWorkOrderMaterialItemUseCase;
    }

    @Override
    @Transactional
    public void handle(AddMaterialToWorkOrderCommand cmd) {
        WorkOrder workOrder = workOrderRepository.findById(cmd.workOrderId())
                .orElseThrow(WorkOrderExceptions.NotFound::new);

        WorkOrderMaterialItem materialItem = createWorkOrderMaterialItemUseCase.handle(
                new CreateWorkOrderMaterialItemCommand(cmd.workOrderId(), cmd.materialId(), cmd.quantity()));

        workOrder.addMaterialItem(materialItem);
        workOrderRepository.save(workOrder);
    }
}
